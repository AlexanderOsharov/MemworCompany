package com.shurik.memwor_24.pizza_planet.other;

import android.text.Editable;
import android.text.TextWatcher;

// класс для "рефакторинга" editPhone в регистрации пользователя (UserFragment)
public class CurrencyTextWatcherEditPhone implements TextWatcher {

    private StringBuilder builder = new StringBuilder();
    private boolean ignore;
    private final char numPlace = 'X';

    // вызывается перед тем, как изменится текст
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    // вызывается во время изменения текста
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    // вызывается после изменения текста в editText
    // Editable - класс, который позволяет изменять текст внутри editText
    @Override
    public void afterTextChanged(Editable s) {
        if (!ignore) {
            removeFormat(s.toString());
            applyFormat(builder.toString()); // форматирование текста
            ignore = true;
            s.replace(0, s.length(), builder.toString()); // введенный тектс полностью заменяется на отформатированный
            ignore = false;
        }
    }

    private void removeFormat(String text) {
        // функция отрезает все лишние символы от строки, которую мы вводим
        builder.setLength(0); // builder очищается
        for (int i = 0; i < text.length(); i++) {
            // в builder помещаются все символы, которые являются цифрами
            char c = text.charAt(i);
            if (isNumberChar(c)) {
                builder.append(c);
            }
        }
    }


    private void applyFormat(String text) {
        // форматирование по шаблону
        String template = getTemplate(text); // выбираем щаблон в зависимости от введенного номер телефона
        builder.setLength(0);
        for (int i = 0, textIndex = 0; i < template.length()
                && textIndex < text.length(); i++) {
            // идем по шаблону и заменяем символы X на соответствующие цифры
            if (template.charAt(i) == numPlace) {
                builder.append(text.charAt(textIndex));
                textIndex++;
            } else {
                builder.append(template.charAt(i));
            }
        }
    }

    private boolean isNumberChar(char c) {
        // является ли символ цифрой?
        return c >= '0' && c <= '9';
    }

    // шаблоны
    private String getTemplate(String text) {
        if (text.startsWith("7")) {
            return "+X (XXX) XXX-XX-XX";
            // Россия - пример: +7 (897) 156-56-01
            // Казахстан - пример: +7 (897) 156-56-01
        }
        return "+7 (XXX) XXX-XX-XX";
    }
}