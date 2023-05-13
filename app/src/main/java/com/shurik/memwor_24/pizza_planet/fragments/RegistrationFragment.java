//package com.shurik.memwor_24.pizza_planet.fragments;
//
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentTransaction;
//
//import com.shurik.memwor_24.R;
//import com.shurik.memwor_24.databinding.FragmentRegistrationBinding;
//import com.shurik.memwor_24.pizza_planet.model.Avatar;
//import com.shurik.memwor_24.pizza_planet.model.Phone;
//import com.shurik.memwor_24.pizza_planet.other.CurrencyTextWatcherEditPhone;
//import com.shurik.memwor_24.pizza_planet.rest.UserAPIVolley;
//import com.shurik.memwor_24.pizza_planet.user_database.UserEntity;
//
//import java.io.IOException;
//
//// фрагмент регистрации
//
//// todo все основательно проверить и сделать выполнение в другом потоке
//public class RegistrationFragment extends Fragment {
//
//    private FragmentRegistrationBinding binding;
//
//    private FragmentManager fm = getActivity().getSupportFragmentManager();
//
//    // фрагмент профиля
//    private ProfileFragment profileFragment = new ProfileFragment();
//
//    // пользователь
//    private UserEntity user;
//
//    // все данные готовы к отправке на сервер и созданию нового пользователя?
//    private boolean check = false;
//
//    private int[] editCheckVals = {0, 0, 0, 0};
//
//    private String[] editVals = {"", "", "", ""};
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        binding = FragmentRegistrationBinding.inflate(inflater, container, false);
//
//        // инициализация userAPIVolley
//        UserAPIVolley userAPIVolley = new UserAPIVolley(getContext(),
//                binding.getRoot(),
//                getLayoutInflater());
//
//        // ставим "форматтер" для телефона
//        binding.editPhoneReg.addTextChangedListener(new CurrencyTextWatcherEditPhone());
//
//        // регистрация
//        binding.regButton.setOnClickListener(m -> {
//
//            // сохраняем введенные данные в переменные
//            String userName = binding.editNameReg.getText().toString();
//            String phone = binding.editPhoneReg.getText().toString();
//            String userMail = binding.editMailReg.getText().toString();
//            String userPassword = binding.editPassReg.getText().toString();
//
//            String userAvatar = "jkfkfkkfkfkfk"; // todo доделать
//
//            // доставщик или нет
//            int customerOrNot = 0;
//
//            // проверка данных
//            try {
//                check = checkUserData(userAPIVolley,
//                        userName,
//                        phone, userMail,
//                        userPassword);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            if (check) {
//                buildUser(user, userName, phone,
//                        userMail, userPassword,
//                        userAvatar, customerOrNot); // создание юзера
//
//                userAPIVolley.addUser(user); // добавление пользователя
//                /**
//                 * 1. если пользователь добавился, то переходим на фрагмент профиля
//                 * 2. если не добавился - "вылетает" snackbar
//                 */
//
//            } else {
//                meaasageWrongData(editCheckVals,
//                        editVals); // готовим текст для сообщений о невалидных данных
//                setMessageWrongData(editVals); // устанавливаем этот текст
//            }
//        });
//
//        return binding.getRoot();
//    }
//
//    // метод, который проверяет данные на соответствие требованиям
//    private boolean checkUserData(UserAPIVolley userAPIVolley,
//                                  String userName,
//                                  String phone,
//                                  String userMail,
//                                  String userPassword) throws IOException {
//
//        int nameVerdict = editVerdictName(userAPIVolley, userName);
//        int phoneVerdict = editVerdictPhone(userAPIVolley, phone);
//        int mailVerdict = editVerdictMail(userAPIVolley, userMail);
//        int addressVerdict = editVerdictPassword(userAPIVolley, userPassword);
//
//        editCheckVals[0] = nameVerdict;
//        editCheckVals[1] = phoneVerdict;
//        editCheckVals[2] = mailVerdict;
//        editCheckVals[3] = addressVerdict;
//
//        if (nameVerdict == 3 &&
//                phoneVerdict == 3 &&
//                mailVerdict == 3 &&
//                addressVerdict == 3) { // все требования учтены
//            return true;
//        } else { // не все требования учтены
//            return false;
//        }
//    }
//
//    /**
//     * далее идут 4 метода для проверки данных
//     */
//
//    // метод, "так скажем" выдающий вердикт имени
//    private int editVerdictName(UserAPIVolley userAPIVolley,
//                                String userName) throws IOException {
//        if (TextUtils.isEmpty(userName)) {
//            return 0; // имя не введено
//        } else if (userAPIVolley.checkUser(userName, "userName")) {
//            return 2; // в бд уже есть такое имя
//        }
//        return 3; // все хорошо
//    }
//
//    // вердикт телефону
//    private int editVerdictPhone(UserAPIVolley userAPIVolley,
//                                 String phone) throws IOException {
//        if (TextUtils.isEmpty(phone)) {
//            return 0; // телефон не введен
//        } else if (rightPhone(phone)) {
//            return 1; // телефон введен неправильно
//        } else if (userAPIVolley.checkUser(phone, "phone")) {
//            return 2; // в бд уже есть ползователь с таким телефоном
//        }
//        return 3; // все хорошо
//    }
//
//    // метод, который проверяет, что введенный телефон правильный
//    private boolean rightPhone(String phone) {
////        if (phone.length() < 18) {
////            return false;
////        } else if () {
////            return false;
////        }
//        return true;
//    }
//
//    // вердикт email - у
//    private int editVerdictMail(UserAPIVolley userAPIVolley,
//                                String userMail) throws IOException {
//        if (TextUtils.isEmpty(userMail)) {
//            return 0; // mail не введен
//        } else if (android.util.Patterns.EMAIL_ADDRESS.matcher(userMail)
//                .matches()) {
//            return 1; // Email введен неправильно
//        } else if (userAPIVolley.checkUser(userMail, "userMail")) {
//            return 2; // в бд уже есть ползователь с таким mail - ом
//        }
//        return 3; // все хорошо
//    }
//
//    // вердикт паролю
//    private int editVerdictPassword(UserAPIVolley userAPIVolley,
//                                    String userPassword) {
//        if (TextUtils.isEmpty(userPassword)) {
//            return 0; // пароль не введен
//        } else if (userPassword.length() < 8) {
//            return 1; // длина пароля должна быть больше 8
//        }
//        return 3; // все хорошо
//    }
//
//    // метод, который "собирает" пользователя из данных
//    private void buildUser(UserEntity user,
//                           String userName,
//                           String phoneNumber,
//                           String userMail,
//                           String userPassword,
//                           String userAvatarUri,
//                           int customerOrNot) {
//
//        // "конструируем телефон и аватарку"
//        Phone phone = new Phone(phoneNumber);
//        Avatar avatar = new Avatar(userAvatarUri, phone);
//
//        // "конструируем" пользовтеля
//        user = new UserEntity(userName,
//                phone, userMail, userPassword, avatar, customerOrNot);
//    }
//
//    /**
//     * метод выдает сообщения о том, что нужно исправить
//     * (пользователь не учел какие либо требования к данным)
//     */
//
//    // todo в этом методе содержится плохо написанный код
//
//    /**
//     * todo | мы были вынуждены его написать для упрощения структуры проверки
//     * todo | заполненных данных
//     */
//
//    private void meaasageWrongData(int[] editCheckVals,
//                                   String editVals[]) {
//        for (int i = 0; i < editCheckVals.length; i++) {
//            if (editCheckVals[i] == 3) {
//                continue;
//
//            } else if (editCheckVals[i] == 1) { // данные введены неправильно
//                if (i == 1) {
//                    editVals[i] = "Введенный телефон неправильный";
//                } else if (i == 2) {
//                    editVals[i] = "Введенный email неправильный";
//                } else if (i == 3) {
//                    editVals[i] = "Длина пароля должна составлять больше 8 символов";
//                }
//
//            } else if (editCheckVals[i] == 2) { // в бд уже есть пользователь с такими данными
//                if (i == 0) {
//                    editVals[i] = "Пользователь с таким именем уже существует";
//                } else if (i == 1) {
//                    editVals[i] = "Пользователь с таким телефоном уже существует";
//                } else if (i == 2) {
//                    editVals[i] = "Пользователь с таким email уже существует";
//                }
//
//            } else if (editCheckVals[i] == 0) { // данные не введены
//                if (i == 0) {
//                    editVals[i] = "Имя не введено";
//                } else if (i == 1) {
//                    editVals[i] = "Телефон не введен";
//                } else if (i == 2) {
//                    editVals[i] = "Email не введен";
//                } else if (i == 3) {
//                    editVals[i] = "Пароль не введен";
//                }
//            }
//        }
//    }
//
//    // установка сообщений о неверных данных
//    private void setMessageWrongData(String editVals[]) {
//        binding.messageName.setText(editVals[0]);
//        binding.messagePhone.setText(editVals[1]);
//        binding.messageMail.setText(editVals[2]);
//        binding.messagePassword.setText(editVals[3]);
//    }
//
//    // метод, который "переходит" на фрагмент с данными пользователя (фрагмент профиля)
//    private void transitionOrNot(UserEntity user,
//                                 FragmentManager fm) {
//        FragmentTransaction ft = fm.beginTransaction();
//
//        user.setInsidePofile(1);
//        // запоминаем: пользователь вошел в акк (в фрагмент профиля)
//
//        ft.replace(R.id.profile_fragment,
//                        profileFragment,
//                        "profileFragment")
//                .addToBackStack(null)
//                .commit();
//    }
//}