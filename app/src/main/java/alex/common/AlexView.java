package alex.common;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import alex.common.voice.AlexVoice;

final public class AlexView {
    static View[] flashScreen, mainScreen;
    static View flashLayout;
    static ImageView flashCar1, flashCar2;
    static String flashSpeak;

    public static void hideAndShow(View[] hide, View[] show) {
        for (View view : hide) view.setVisibility(View.GONE);
        for (View view : show) view.setVisibility(View.VISIBLE);
    }

    public static void hideAndShow(View hide, View show) {
        hide.setVisibility(View.GONE);
        show.setVisibility(View.VISIBLE);
    }

    public static void toggleKeyboard(Context context, View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInputFromWindow(view.getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
        }
    }

    public static void showFlashScreen(LinearLayout _flashLayout, ImageView _flashCar1, ImageView _flashCar2, View[] _mainScreen, String _toSpeak) {
        flashLayout = _flashLayout;
        mainScreen = _mainScreen;
        flashCar1 = _flashCar1;
        flashCar2 = _flashCar2;
        flashSpeak = _toSpeak;
        flashScreen = new View[]{flashLayout};
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(AlexView::showFlashScreen, 1000);
    }

    static private void showFlashScreen() {
        String toSpeak = flashSpeak;
        AlexVoice.say(toSpeak);
        flashLayout.setVisibility(View.VISIBLE);
        int animate = 1000, delay = 3000;
        float shift = 200;
        float car1x = flashCar1.getX();
        float car2x = flashCar2.getX();
        ObjectAnimator carAnimator1 = ObjectAnimator.ofFloat(flashCar1, "x", car1x - shift, car1x).setDuration(animate);
        ObjectAnimator carAnimator2 = ObjectAnimator.ofFloat(flashCar2, "x", car2x + shift, car2x).setDuration(animate);
        carAnimator1.setInterpolator(new AccelerateInterpolator());
        carAnimator2.setInterpolator(new AccelerateInterpolator());
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(carAnimator1).with(carAnimator2);
        animatorSet.start();
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(AlexView::hideFlashScreen, delay);
    }

    static private void hideFlashScreen() {
        AlexView.hideAndShow(flashScreen, mainScreen);
    }
}