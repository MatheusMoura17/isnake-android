package me.furtado.isnake.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import me.furtado.isnake.R;

/**
 * Created by bfurtado on 10/01/16.
 */
public class GameOverDialog extends Dialog {

    private Button mBtRetry;

    public GameOverDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_game_over);

        //
        // fundo transparente
        //

        ColorDrawable colorDrawable = new ColorDrawable(Color.TRANSPARENT);

        Window window = getWindow();
        window.setBackgroundDrawable(colorDrawable);

        //
        // configuranco botoes
        //

        mBtRetry = (Button) findViewById(R.id.bt_retry);
        mBtRetry.setOnClickListener(mBtRetryListener);
    }

    Button.OnClickListener mBtRetryListener = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            dismiss();
        }
    };
}
