package com.shurik.memwor_24.pizza_planet.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.viewmodel.CreationExtras;

import com.shurik.memwor_24.R;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.RequestPoint;
import com.yandex.mapkit.RequestPointType;
import com.yandex.mapkit.directions.DirectionsFactory;
import com.yandex.mapkit.directions.driving.DrivingOptions;
import com.yandex.mapkit.directions.driving.DrivingRoute;
import com.yandex.mapkit.directions.driving.DrivingRouter;
import com.yandex.mapkit.directions.driving.DrivingSession;
import com.yandex.mapkit.directions.driving.VehicleOptions;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.PolylineMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.transport.masstransit.Route;
import com.yandex.mapkit.transport.masstransit.Session;
import com.yandex.runtime.Error;
import com.yandex.runtime.image.ImageProvider;
import com.yandex.runtime.network.NetworkError;
import com.yandex.runtime.network.RemoteError;

import java.util.ArrayList;
import java.util.List;

public class MapDialogFragment extends DialogFragment implements Session.RouteListener, DrivingSession.DrivingRouteListener {

    // view - шки
    private MapView mapView;
    private ImageButton closeButton;

    // точки
    private Point currentPoint; // Текущие координаты
    private Point destinationPoint; // Координаты ресторана
    private Point SCREEN_CENTER;

    private MapObjectCollection mapObjects;
    /**
     * Коллекция объектов карты,
     * которая может содержать любой набор элементов объектов карты,
     * включая вложенные коллекции.
     */

    /**
     * PlacemarkMapObject - объект с географическим расположением на карте
     */
    private PlacemarkMapObject currentPointPlacemark = null; // метка текущей точки
    private PlacemarkMapObject destinationPointPlacemark = null; // метка точки назначения

    private DrivingRouter drivingRouter;
    /**
     * DrivingRouter - интерфейс для маршрутизатора
     */

    /**
     * Заметка:
     * Session - интерфейс, обозначающий текущий сеанс поиска (или прохождения мршрута).
     * Позволяет отменить поиск и повторить попытку.
     */
    private DrivingSession drivingSession;

    // массив, содержащий определенные цвета
    // TODO зачем так много цветов
    private int[] routeColors = {Color.BLUE,
            Color.GREEN, Color.RED,
            Color.YELLOW, Color.MAGENTA, Color.CYAN};

    /**
     * // метод для создания фрагмента
     * (по факту - это диалоговое окно) с нужными точками
     */
    public static MapDialogFragment newInstance(Point currentPoint,
                                                Point destinationPoint) {
        MapDialogFragment fragment = new MapDialogFragment();

        // создаем пакет для данных
        Bundle args = new Bundle();

        // сохраняем координаты текущего местоположения
        args.putFloatArray("currentPoint", new float[]{
                (float) currentPoint.getLatitude(),
                (float) currentPoint.getLongitude()
        });

        // сохраняем координаты точки назначения
        args.putFloatArray("destinationPoint", new float[]{
                (float) destinationPoint.getLatitude(),
                (float) destinationPoint.getLongitude()
        });

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NO_TITLE,
                android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth);

        if (getArguments() != null) {
            // извлекаем данные и сохрнаяем их в массивы
            float[] currentCoords = getArguments().
                    getFloatArray("currentPoint");
            float[] destinationCoords = getArguments().
                    getFloatArray("destinationPoint");

            // создаем точки
            currentPoint = new Point(currentCoords[0], currentCoords[1]);
            destinationPoint = new Point(destinationCoords[0], destinationCoords[1]);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.dialog_map, container, false);

        mapView = contentView.findViewById(R.id.map_view);

        /**
         * переход на точку текущего метоположения на карте
         * с приближением карты и анимацией
         */
        mapView.getMap()
                .move(
                        new CameraPosition(currentPoint, 14, 0, 0),
                        new Animation(Animation.Type.SMOOTH, 5f),
                        null);

        // инициализация некоторых объектов
        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter();
        mapObjects = mapView.getMap().getMapObjects().addCollection();

        // строим маршрут
        buildDriving();

        // bitmap текущего местоположения
        Bitmap currentLocationBitmap = BitmapFactory.
                decodeResource(getResources(), R.drawable.current_location_icon);

        Bitmap resizedCurrentLocationBitmap = Bitmap
                .createScaledBitmap(
                        currentLocationBitmap, 60, 60, false);

        ImageProvider currentLocationImage = ImageProvider.fromBitmap(
                resizedCurrentLocationBitmap);

        // bitmap точки назначения
        Bitmap destinationLocationBitmap = BitmapFactory.decodeResource
                (getResources(), R.drawable.destination_location_icon);

        Bitmap resizedDestinationLocationBitmap = Bitmap.
                createScaledBitmap(
                        destinationLocationBitmap, 60, 60, false);

        ImageProvider destinationLocationImage = ImageProvider.
                fromBitmap(resizedDestinationLocationBitmap);

        currentPointPlacemark = mapObjects.addPlacemark(currentPoint,
                currentLocationImage);

        destinationPointPlacemark = mapObjects.addPlacemark(destinationPoint,
                destinationLocationImage);

        SCREEN_CENTER = new Point(
                (currentPoint.getLatitude() + destinationPoint.getLatitude()) / 2,
                (currentPoint.getLongitude() + destinationPoint.getLongitude()) / 2
        );

        // закрытие карты
        closeButton = contentView.findViewById(R.id.close_map);
        closeButton.setOnClickListener(view -> dismiss());
        return contentView;
    }

    /**
     * onStart(), onStop(), onDestroyView() - есди что - то происходит с фргментом,
     * то это же и происходит с даологовым окошком
     */

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
        MapKitFactory.getInstance().onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
    }

    @Override
    public void onDestroyView() {
        mapView.onStop();
        super.onDestroyView();
    }

    @Override
    public void onMasstransitRoutes(@NonNull List<Route> routes) {

    }

    @Override
    public void onMasstransitRoutesError(@NonNull Error error) {
        if (error instanceof RemoteError) {
            System.out.println("Failed to load route: Remote error");
        } else if (error instanceof NetworkError) {
            System.out.println("Failed to load route: Network error");
        } else {
            System.out.println("Failed to load route: Unknown error");
        }
    }

    int colorIndex = 0; // начинаем с первого цвета

    // маршруты движения
    @Override
    public void onDrivingRoutes(@NonNull List<DrivingRoute> list) {

        for (DrivingRoute route : list) {
            // установка цвета линии
            PolylineMapObject polyline = mapObjects.addPolyline(
                    route.getGeometry());
            polyline.setStrokeColor(routeColors[colorIndex]);
            colorIndex++; // переход к следующему цвету
            if (colorIndex >= routeColors.length) {
                colorIndex = 0; // обнуление счетчика
            }
        }
    }

    @Override
    public void onDrivingRoutesError(@NonNull Error error) {
        String errorMessage = "Неизвестная ошибка";
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    // метод для "построения" нашего маршрута
    private void buildDriving() {
        DrivingOptions drivingOptions = new DrivingOptions();
        VehicleOptions vehicleOptions = new VehicleOptions();
        ArrayList<RequestPoint> requestPoints = new ArrayList();

        requestPoints.add(new RequestPoint(currentPoint,
                RequestPointType.WAYPOINT, null));
        requestPoints.add(new RequestPoint(destinationPoint,
                RequestPointType.WAYPOINT, null));
        drivingSession = drivingRouter.requestRoutes(requestPoints,
                drivingOptions, vehicleOptions, this);
    }

    @NonNull
    @Override
    public CreationExtras getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }
}