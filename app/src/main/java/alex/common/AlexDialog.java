package alex.common;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.widget.EditText;

public class AlexDialog {

    /***
     * prompt - create a prompt dialog
     *
     * Example:
     *   EditText editText = new EditText(this);
     *   AlexDialog.prompt("Enter name", this, editText).setPositiveButton("OK", (dialog, which) -> {
     *      String input = editText.getText().toString();
     *      Toast.makeText(this, input, Toast.LENGTH_LONG).show();
     *   }).show();
     */
    public static AlertDialog.Builder prompt(String title, Context _this, EditText input) {
        AlertDialog.Builder builder = new AlertDialog.Builder(_this);
        builder.setTitle(title);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        input.setPadding(10, 10, 10, 10);
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        return builder;
    }
}
