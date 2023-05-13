//package com.shurik.memwor_24.pizza_planet.fragments;
//
//import android.Manifest;
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.fragment.app.Fragment;
//
//import com.shurik.memwor_24.R;
//import com.shurik.memwor_24.databinding.FragmentProfileBinding;
//
///**
// * фрагмент профиля пользователя -
// * "грубо говоря",
// * фрагмент с данными пользовтеля (их мы тоже будем доставать с сервера)
// */
//public class ProfileFragment extends Fragment {
//
//    private FragmentProfileBinding binding;
//
//    // константы для работы с камерой и директорией с картинками
//    private final int PERMISSION_REQ_CODE_1 = 10;
//    private final int PERMISSION_REQ_CODE_2 = 11;
//    private final int pic_id = 555;
//    private final int gal_id = 666;
//
//    // intent для камеры
//    private Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//    //  intent для директории с изображениями
//    private Intent intent_file = new Intent();
//
//    // диалоговое окно
//    private Dialog dialog;
//
//    // изменял аватарку или нет
//    private int change = 0;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        binding = FragmentProfileBinding.inflate(inflater, container, false);
//
//        // тыкаем на картинку - появляется диалоговое окно
//        binding.avatar.setOnClickListener(v -> {
//            showDialogSelect(getContext());
//        });
//
//        return binding.getRoot();
//    }
//
//    // далее идет ряд методов по работы с аватаркой
//
//    // проверка на permission для камеры
//    private boolean allPermissionsGrantedCamera() {
//        return ContextCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.CAMERA
//        ) == PackageManager.PERMISSION_GRANTED;
//    }
//
//    // проверка на permission для директории с картинками
//    private boolean allPermissionsGrantedGal() {
//        return ContextCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.READ_EXTERNAL_STORAGE
//        ) == PackageManager.PERMISSION_GRANTED;
//    }
//
//    // ставим аватарку - камера
//    private void doAvatar() {
//        if (allPermissionsGrantedCamera()) {
//            // permissions предоставлены
//            startCamera(); // запуск камеры
//        } else {
//            // если permissions не предоставлены - запрашиваем - вылетает окошко
//            ActivityCompat.requestPermissions(
//                    (Activity) requireContext(),
//                    new String[]{Manifest.permission.CAMERA}, // список запрашиваемых permissions
//                    PERMISSION_REQ_CODE_1
//            );
//        }
//        // сохранение фото происходит автоматом, ибо вы делаете фотку для галереи
//    }
//
//    // ставим аватарку - файлик
//    private void openFile() {
//        if (allPermissionsGrantedGal()) {
//            startDirectoryImages();
//        } else {
//            // если permissions не предоставлены - запрашиваем - вылетает окошко
//            ActivityCompat.requestPermissions(
//                    (Activity) requireContext(),
//                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                    PERMISSION_REQ_CODE_2
//            );
//        }
//        // сохранение фото происходит автоматом, ибо вы делаете фотку для галереи
//    }
//
//    // делаем photo
//    private void startCamera() {
//        startActivityForResult(camera_intent, pic_id);
//        // теперь мы на экране с камерой
//    }
//
//    // открывается директория с файликами, мы выбираем картинку
//    private void startDirectoryImages() {
//        intent_file.setType("image/*");
//        intent_file.setAction(Intent.ACTION_OPEN_DOCUMENT);
//        startActivityForResult(Intent.createChooser(intent_file, "Task image_file"), gal_id);
//        // теперь мы на экране с директорией
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == pic_id) {
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            remove();
//            binding.avatar.setImageBitmap(photo);
//        } else if (requestCode == gal_id) {
//            Uri uri = data.getData();
//            remove();
//            binding.avatar.setImageURI(uri);
//        }
//    }
//
//    // удаляем текст и фон c аватарки
//    private void remove() {
//        if (change == 0) {
//            binding.photo.setVisibility(View.GONE);
//            binding.fonCamera.setVisibility(View.GONE);
//        }
//        ++change;
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode == PERMISSION_REQ_CODE_1) {
//            if (allPermissionsGrantedCamera()) {
//                /**
//                 * если после запроса на permissions они предоставлены,
//                 * то мы запускам камеру
//                 */
//                startCamera();
//            }
//            // если не предоставлены - просто исчезает разрешение
//        } else if (requestCode == PERMISSION_REQ_CODE_2) {
//            if (allPermissionsGrantedGal()) {
//                startDirectoryImages();
//            }
//        }
//    }
//
//    // создание диалогового окна для выбора способа "постановки аватарки"
//    private void showDialogSelect(Context context) {
//        dialog = new Dialog(context);
//        dialog.setContentView(R.layout.alert_registration);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//        TextView select_1;
//        TextView select_2;
//
//        select_1 = dialog.findViewById(R.id.select_1);
//        select_2 = dialog.findViewById(R.id.select_2);
//
//        select_1.setOnClickListener(v -> {
//            dialog.dismiss();
//            doAvatar(); // ставим фотку - фоткаем на камеру
//        });
//        select_2.setOnClickListener(v -> {
//            dialog.dismiss();
//            openFile(); // ставим фотку как файлик (например, из галереи)
//        });
//        dialog.show();
//    }
//}