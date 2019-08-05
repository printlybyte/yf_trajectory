package com.caitiaobang.core.app.tools.nice_spinner;

import android.text.Spannable;

public interface SpinnerTextFormatter<T> {

    Spannable format(T item);
}
