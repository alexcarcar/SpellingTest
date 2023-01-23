package alex.common;

import android.view.View;

final public class AlexView {
    public static void hideAndShow(View[] hide, View[] show) {
        for (View view : hide) view.setVisibility(View.GONE);
        for (View view : show) view.setVisibility(View.VISIBLE);
    }
}
