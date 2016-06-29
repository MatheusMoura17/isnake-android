package me.furtado.isnake.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Timer;
import java.util.TimerTask;

import me.furtado.isnake.BuildConfig;
import me.furtado.isnake.R;
import me.furtado.isnake.dialog.GameOverDialog;
import me.furtado.isnake.helper.AppHelper;
import me.furtado.isnake.helper.OnSwipeListener;
import me.furtado.isnake.helper.Preferences;

public class GameActivity extends AppActivity {

    private static final int INITIAL_SPEED = 80;

    private static int Speed;
    private static int StepSize;
    private static int X;
    private static int Y;

    private static int CurrentScore;
    private static int Step;

    private static boolean IsPlaying;
    private static boolean IsFirstPlay;
    private static boolean IsSideMovement;
    private static boolean Snake06InPlay, Snake07InPlay, Snake08InPlay, Snake09InPlay, Snake10InPlay,
            Snake11InPlay, Snake12InPlay, Snake13InPlay, Snake14InPlay, Snake15InPlay, Snake16InPlay,
            Snake17InPlay, Snake18InPlay, Snake19InPlay, Snake20InPlay, Snake21InPlay, Snake22InPlay,
            Snake23InPlay, Snake24InPlay, Snake25InPlay, Snake26InPlay, Snake27InPlay, Snake28InPlay,
            Snake29InPlay, Snake30InPlay, Snake31InPlay, Snake32InPlay, Snake33InPlay, Snake34InPlay,
            Snake35InPlay, Snake36InPlay, Snake37InPlay, Snake38InPlay, Snake39InPlay, Snake40InPlay,
            Snake41InPlay, Snake42InPlay, Snake43InPlay, Snake44InPlay, Snake45InPlay, Snake46InPlay,
            Snake47InPlay, Snake48InPlay, Snake49InPlay, Snake50InPlay, Snake51InPlay, Snake52InPlay,
            Snake53InPlay, Snake54InPlay, Snake55InPlay, Snake56InPlay, Snake57InPlay, Snake58InPlay,
            Snake59InPlay, Snake60InPlay, Snake61InPlay, Snake62InPlay, Snake63InPlay, Snake64InPlay,
            Snake65InPlay;

    private Timer mTimer;
    private GestureDetectorCompat mGestureDetector;

    private TextView mTxtScore;
    private TextView mTxtBest;
    private ImageButton mBtPlayPause;

    private View mVwCenter;
    private ImageView mImgFood;
    private ImageView mImgSnake01, mImgSnake02, mImgSnake03, mImgSnake04, mImgSnake05, mImgSnake06,
            mImgSnake07, mImgSnake08, mImgSnake09, mImgSnake10, mImgSnake11, mImgSnake12,
            mImgSnake13, mImgSnake14, mImgSnake15, mImgSnake16, mImgSnake17, mImgSnake18,
            mImgSnake19, mImgSnake20, mImgSnake21, mImgSnake22, mImgSnake23, mImgSnake24,
            mImgSnake25, mImgSnake26, mImgSnake27, mImgSnake28, mImgSnake29, mImgSnake30,
            mImgSnake31, mImgSnake32, mImgSnake33, mImgSnake34, mImgSnake35, mImgSnake36,
            mImgSnake37, mImgSnake38, mImgSnake39, mImgSnake40, mImgSnake41, mImgSnake42,
            mImgSnake43, mImgSnake44, mImgSnake45, mImgSnake46, mImgSnake47, mImgSnake48,
            mImgSnake49, mImgSnake50, mImgSnake51, mImgSnake52, mImgSnake53, mImgSnake54,
            mImgSnake55, mImgSnake56, mImgSnake57, mImgSnake58, mImgSnake59, mImgSnake60,
            mImgSnake61, mImgSnake62, mImgSnake63, mImgSnake64, mImgSnake65;

    private Button mBtFooter;
    private LinearLayout mLlGuide;
    private TextView mTxtVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //
        // configurando views
        //

        loadView();
        viewDidLoad();

        //
        // logica do jogo
        //

        IsFirstPlay = true;
        stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pause();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    //
    // PRIVATE METHODS
    //

    private void gameOver() {
        mBtPlayPause.setVisibility(View.GONE);

        //
        // verificando melhor pontuacao
        //

        int bestScore = Preferences.getBestScore(this);

        if (CurrentScore > bestScore) {
            Preferences.setBestScore(bestScore, this);
            setBestScore(bestScore);
        }

        //
        // zera tudo
        //

        stop();

        //
        // apresenta view de game over
        //

        ColorDrawable colorDrawable = new ColorDrawable(Color.TRANSPARENT);

        GameOverDialog gameOverDialog = new GameOverDialog(this);
        gameOverDialog.show();
    }

    private void pause() {
        IsPlaying = false;

        mBtPlayPause.setSelected(true);
        mBtFooter.setVisibility(View.VISIBLE);

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    private void play() {
        IsPlaying = true;

        mBtFooter.setVisibility(View.GONE);
        mBtPlayPause.setSelected(false);

        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        move();
                    }
                });
            }
        }, 0, Speed);
    }

    private void start() {
        mLlGuide.setVisibility(View.GONE);
        mBtPlayPause.setVisibility(View.VISIBLE);

        String text = getString(R.string.tap_to_continue);
        mBtFooter.setText(text);

        Speed = INITIAL_SPEED;
        IsSideMovement = true;

        play();

        mImgFood.setVisibility(View.VISIBLE);
    }

    private void stop() {
        pause();

        mBtPlayPause.setVisibility(View.GONE);
        mImgFood.setVisibility(View.GONE);
        mLlGuide.setVisibility(View.GONE);

        Speed = 0;

        CurrentScore = 0;
        setCurrentScore(0);

        moveRight();
    }


    private void move() {

        int x, y;

        //
        // movimentando as partes da cobra
        //

        mImgSnake65.setX(mImgSnake64.getX()); mImgSnake65.setY(mImgSnake64.getY());
        mImgSnake64.setX(mImgSnake63.getX()); mImgSnake64.setY(mImgSnake63.getY());
        mImgSnake63.setX(mImgSnake62.getX()); mImgSnake63.setY(mImgSnake62.getY());
        mImgSnake62.setX(mImgSnake61.getX()); mImgSnake62.setY(mImgSnake61.getY());
        mImgSnake61.setX(mImgSnake60.getX()); mImgSnake61.setY(mImgSnake60.getY());
        mImgSnake60.setX(mImgSnake59.getX()); mImgSnake60.setY(mImgSnake59.getY());
        mImgSnake59.setX(mImgSnake58.getX()); mImgSnake59.setY(mImgSnake58.getY());
        mImgSnake58.setX(mImgSnake57.getX()); mImgSnake58.setY(mImgSnake57.getY());
        mImgSnake57.setX(mImgSnake56.getX()); mImgSnake57.setY(mImgSnake56.getY());
        mImgSnake56.setX(mImgSnake55.getX()); mImgSnake56.setY(mImgSnake55.getY());
        mImgSnake55.setX(mImgSnake54.getX()); mImgSnake55.setY(mImgSnake54.getY());
        mImgSnake54.setX(mImgSnake53.getX()); mImgSnake54.setY(mImgSnake53.getY());
        mImgSnake53.setX(mImgSnake52.getX()); mImgSnake53.setY(mImgSnake52.getY());
        mImgSnake52.setX(mImgSnake51.getX()); mImgSnake52.setY(mImgSnake51.getY());
        mImgSnake51.setX(mImgSnake50.getX()); mImgSnake51.setY(mImgSnake50.getY());
        mImgSnake50.setX(mImgSnake49.getX()); mImgSnake50.setY(mImgSnake49.getY());
        mImgSnake49.setX(mImgSnake48.getX()); mImgSnake49.setY(mImgSnake48.getY());
        mImgSnake48.setX(mImgSnake47.getX()); mImgSnake48.setY(mImgSnake47.getY());
        mImgSnake47.setX(mImgSnake46.getX()); mImgSnake47.setY(mImgSnake46.getY());
        mImgSnake46.setX(mImgSnake45.getX()); mImgSnake46.setY(mImgSnake45.getY());
        mImgSnake45.setX(mImgSnake44.getX()); mImgSnake45.setY(mImgSnake44.getY());
        mImgSnake44.setX(mImgSnake43.getX()); mImgSnake44.setY(mImgSnake43.getY());
        mImgSnake43.setX(mImgSnake42.getX()); mImgSnake43.setY(mImgSnake42.getY());
        mImgSnake42.setX(mImgSnake41.getX()); mImgSnake42.setY(mImgSnake41.getY());
        mImgSnake41.setX(mImgSnake40.getX()); mImgSnake41.setY(mImgSnake40.getY());
        mImgSnake40.setX(mImgSnake39.getX()); mImgSnake40.setY(mImgSnake39.getY());
        mImgSnake39.setX(mImgSnake38.getX()); mImgSnake39.setY(mImgSnake38.getY());
        mImgSnake38.setX(mImgSnake37.getX()); mImgSnake38.setY(mImgSnake37.getY());
        mImgSnake37.setX(mImgSnake36.getX()); mImgSnake37.setY(mImgSnake36.getY());
        mImgSnake36.setX(mImgSnake35.getX()); mImgSnake36.setY(mImgSnake35.getY());
        mImgSnake35.setX(mImgSnake34.getX()); mImgSnake35.setY(mImgSnake34.getY());
        mImgSnake34.setX(mImgSnake33.getX()); mImgSnake34.setY(mImgSnake33.getY());
        mImgSnake33.setX(mImgSnake32.getX()); mImgSnake33.setY(mImgSnake32.getY());
        mImgSnake32.setX(mImgSnake31.getX()); mImgSnake32.setY(mImgSnake31.getY());
        mImgSnake31.setX(mImgSnake30.getX()); mImgSnake31.setY(mImgSnake30.getY());
        mImgSnake30.setX(mImgSnake29.getX()); mImgSnake30.setY(mImgSnake29.getY());
        mImgSnake29.setX(mImgSnake28.getX()); mImgSnake29.setY(mImgSnake28.getY());
        mImgSnake28.setX(mImgSnake27.getX()); mImgSnake28.setY(mImgSnake27.getY());
        mImgSnake27.setX(mImgSnake26.getX()); mImgSnake27.setY(mImgSnake26.getY());
        mImgSnake26.setX(mImgSnake25.getX()); mImgSnake26.setY(mImgSnake25.getY());
        mImgSnake25.setX(mImgSnake24.getX()); mImgSnake25.setY(mImgSnake24.getY());
        mImgSnake24.setX(mImgSnake23.getX()); mImgSnake24.setY(mImgSnake23.getY());
        mImgSnake23.setX(mImgSnake22.getX()); mImgSnake23.setY(mImgSnake22.getY());
        mImgSnake22.setX(mImgSnake21.getX()); mImgSnake22.setY(mImgSnake21.getY());
        mImgSnake21.setX(mImgSnake20.getX()); mImgSnake21.setY(mImgSnake20.getY());
        mImgSnake20.setX(mImgSnake19.getX()); mImgSnake20.setY(mImgSnake19.getY());
        mImgSnake19.setX(mImgSnake18.getX()); mImgSnake19.setY(mImgSnake18.getY());
        mImgSnake18.setX(mImgSnake17.getX()); mImgSnake18.setY(mImgSnake17.getY());
        mImgSnake17.setX(mImgSnake16.getX()); mImgSnake17.setY(mImgSnake16.getY());
        mImgSnake16.setX(mImgSnake15.getX()); mImgSnake16.setY(mImgSnake15.getY());
        mImgSnake15.setX(mImgSnake14.getX()); mImgSnake15.setY(mImgSnake14.getY());
        mImgSnake14.setX(mImgSnake13.getX()); mImgSnake14.setY(mImgSnake13.getY());
        mImgSnake13.setX(mImgSnake12.getX()); mImgSnake13.setY(mImgSnake12.getY());
        mImgSnake12.setX(mImgSnake11.getX()); mImgSnake12.setY(mImgSnake11.getY());
        mImgSnake11.setX(mImgSnake10.getX()); mImgSnake11.setY(mImgSnake10.getY());
        mImgSnake10.setX(mImgSnake09.getX()); mImgSnake10.setY(mImgSnake09.getY());
        mImgSnake09.setX(mImgSnake08.getX()); mImgSnake09.setY(mImgSnake08.getY());
        mImgSnake08.setX(mImgSnake07.getX()); mImgSnake08.setY(mImgSnake07.getY());
        mImgSnake07.setX(mImgSnake06.getX()); mImgSnake07.setY(mImgSnake06.getY());
        mImgSnake06.setX(mImgSnake05.getX()); mImgSnake06.setY(mImgSnake05.getY());
        mImgSnake05.setX(mImgSnake04.getX()); mImgSnake05.setY(mImgSnake04.getY());
        mImgSnake04.setX(mImgSnake03.getX()); mImgSnake04.setY(mImgSnake03.getY());
        mImgSnake03.setX(mImgSnake02.getX()); mImgSnake03.setY(mImgSnake02.getY());
        mImgSnake02.setX(mImgSnake01.getX()); mImgSnake02.setY(mImgSnake01.getY());
        mImgSnake01.setX(mImgSnake01.getX()+X); mImgSnake01.setY(mImgSnake01.getY()+Y);

        //
        // verificando se a cobra foi alimentada
        //

        if (AppHelper.viewsIntersect(mImgSnake01, mImgFood)) {
            putFood();
            updateScore();
        }

        //
        // caso as partes do corpo se batam
        //

        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake03)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake04)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake05)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake06) && (Snake06InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake07) && (Snake07InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake08) && (Snake08InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake09) && (Snake09InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake10) && (Snake10InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake11) && (Snake11InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake12) && (Snake12InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake13) && (Snake13InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake14) && (Snake14InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake15) && (Snake15InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake16) && (Snake16InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake07) && (Snake17InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake18) && (Snake18InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake19) && (Snake19InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake20) && (Snake20InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake21) && (Snake21InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake22) && (Snake22InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake23) && (Snake23InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake24) && (Snake24InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake25) && (Snake25InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake26) && (Snake26InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake27) && (Snake27InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake28) && (Snake28InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake29) && (Snake29InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake30) && (Snake30InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake31) && (Snake31InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake32) && (Snake32InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake33) && (Snake33InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake34) && (Snake34InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake35) && (Snake35InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake36) && (Snake36InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake37) && (Snake37InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake38) && (Snake38InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake39) && (Snake39InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake40) && (Snake40InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake41) && (Snake41InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake42) && (Snake42InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake43) && (Snake43InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake44) && (Snake44InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake45) && (Snake45InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake46) && (Snake46InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake47) && (Snake47InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake48) && (Snake48InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake49) && (Snake49InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake50) && (Snake50InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake51) && (Snake51InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake52) && (Snake52InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake53) && (Snake53InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake54) && (Snake54InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake55) && (Snake55InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake56) && (Snake56InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake57) && (Snake57InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake58) && (Snake58InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake59) && (Snake59InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake60) && (Snake60InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake61) && (Snake61InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake62) && (Snake62InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake63) && (Snake63InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake64) && (Snake64InPlay)) {
            gameOver();
        }
        if (AppHelper.viewsIntersect(mImgSnake01, mImgSnake65) && (Snake65InPlay)) {
            gameOver();
        }

        //
        // limites da tela
        //

        float currentPosition;
        float viewLimit;

        // <-
        currentPosition = mImgSnake01.getX();
        viewLimit = 0;
        if (currentPosition <= viewLimit) {
            mImgSnake01.setX(mVwCenter.getWidth() - 1);
        }

        // ->
        currentPosition = mImgSnake01.getX();
        viewLimit = mVwCenter.getWidth();
        if (currentPosition > viewLimit) {
            mImgSnake01.setX(0);
        }

        // top
        currentPosition = mImgSnake01.getY();
        viewLimit = 0;
        if (currentPosition <= viewLimit) {
            mImgSnake01.setY(mVwCenter.getHeight()-1);
        }

        // bottom
        currentPosition = mImgSnake01.getY();
        viewLimit = mVwCenter.getHeight();
        if (currentPosition > viewLimit) {
            mImgSnake01.setY(0);
        }
    }

    private void moveUp() {
        if (IsSideMovement) {
            X = 0;
            Y = (-1 * StepSize);
        }
    }

    private void moveDown() {
        if (IsSideMovement) {
            X = 0;
            Y = StepSize;
        }
    }

    private void moveLeft() {
        if (IsSideMovement == false) {
            X = (-1 * StepSize);
            Y = 0;
        }
    }

    private void moveRight() {
        if (IsSideMovement == false) {
            X = StepSize;
            Y = 0;
        }
    }


    private void putFood() {
        int x, y;
        int minRange, maxRange;

        while (true) {

            //
            // eixo x
            //

            int foodWidth = mImgFood.getWidth();
            int snakeWidth = mImgSnake01.getWidth();

            minRange = (int) (foodWidth * 1.5f);
            maxRange = mVwCenter.getWidth() - minRange;
            x = AppHelper.randomNumberWithRange(minRange, maxRange);

            if (x < foodWidth) {
                x = (int) (snakeWidth * 1.5f);
            }

            //
            // eixo y
            //

            int foodHeight = mImgFood.getHeight();
            int snakeHeight = mImgSnake01.getHeight();

            minRange = (int) (foodHeight * 1.5f);
            maxRange = mVwCenter.getHeight() - minRange;

            y = AppHelper.randomNumberWithRange(minRange, maxRange);

            if (y < foodHeight) {
                y = (int) (snakeHeight * 1.5f);
            }

            //
            // definindo a posicao
            //

            View vwBody = new View(this);
            vwBody.setLayoutParams(mImgSnake02.getLayoutParams());
            vwBody.setX(x);
            vwBody.setY(y);

            if (AppHelper.viewsIntersect(vwBody, mImgSnake01)) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake02)) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake03)) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake04)) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake05)) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake06) && Snake06InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake07) && Snake07InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake08) && Snake08InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake09) && Snake09InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake10) && Snake10InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake11) && Snake11InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake12) && Snake12InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake13) && Snake13InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake14) && Snake14InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake15) && Snake15InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake16) && Snake16InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake17) && Snake17InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake18) && Snake18InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake19) && Snake19InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake20) && Snake20InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake21) && Snake21InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake22) && Snake22InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake23) && Snake23InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake24) && Snake24InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake25) && Snake25InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake26) && Snake26InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake27) && Snake27InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake28) && Snake28InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake29) && Snake29InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake30) && Snake30InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake31) && Snake31InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake32) && Snake32InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake33) && Snake33InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake34) && Snake34InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake35) && Snake35InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake36) && Snake36InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake37) && Snake37InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake38) && Snake38InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake39) && Snake39InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake40) && Snake40InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake41) && Snake41InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake42) && Snake42InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake43) && Snake43InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake44) && Snake44InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake45) && Snake45InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake46) && Snake46InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake47) && Snake47InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake48) && Snake48InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake49) && Snake49InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake50) && Snake50InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake51) && Snake51InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake52) && Snake52InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake53) && Snake53InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake54) && Snake54InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake55) && Snake55InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake56) && Snake56InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake57) && Snake57InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake58) && Snake58InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake59) && Snake59InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake60) && Snake60InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake61) && Snake61InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake62) && Snake62InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake63) && Snake63InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake64) && Snake64InPlay) continue;
            if (AppHelper.viewsIntersect(vwBody, mImgSnake65) && Snake65InPlay) continue;

            mImgFood.setX(x);
            mImgFood.setY(y);

            break;
        }
    }

    private void updateScore() {
        CurrentScore += 10;
        setCurrentScore(CurrentScore);

        Step++;

        switch (Step) {
            case 1: mImgSnake06.setVisibility(View.VISIBLE); Snake06InPlay = true; break;
            case 2: mImgSnake07.setVisibility(View.VISIBLE); Snake07InPlay = true; break;
            case 3: mImgSnake08.setVisibility(View.VISIBLE); Snake08InPlay = true; break;
            case 4: mImgSnake09.setVisibility(View.VISIBLE); Snake09InPlay = true; break;
            case 5: mImgSnake10.setVisibility(View.VISIBLE); Snake10InPlay = true; break;
            case 6: mImgSnake11.setVisibility(View.VISIBLE); Snake11InPlay = true; break;
            case 7: mImgSnake12.setVisibility(View.VISIBLE); Snake12InPlay = true; break;
            case 8: mImgSnake13.setVisibility(View.VISIBLE); Snake13InPlay = true; break;
            case 9: mImgSnake14.setVisibility(View.VISIBLE); Snake14InPlay = true; break;
            case 10: mImgSnake15.setVisibility(View.VISIBLE); Snake15InPlay = true; break;
            case 11: mImgSnake16.setVisibility(View.VISIBLE); Snake16InPlay = true; break;
            case 12: mImgSnake17.setVisibility(View.VISIBLE); Snake17InPlay = true; break;
            case 13: mImgSnake18.setVisibility(View.VISIBLE); Snake18InPlay = true; break;
            case 14: mImgSnake19.setVisibility(View.VISIBLE); Snake19InPlay = true; break;
            case 15: mImgSnake20.setVisibility(View.VISIBLE); Snake20InPlay = true; break;
            case 16: mImgSnake21.setVisibility(View.VISIBLE); Snake21InPlay = true; break;
            case 17: mImgSnake22.setVisibility(View.VISIBLE); Snake22InPlay = true; break;
            case 18: mImgSnake23.setVisibility(View.VISIBLE); Snake23InPlay = true; break;
            case 19: mImgSnake24.setVisibility(View.VISIBLE); Snake24InPlay = true; break;
            case 20: mImgSnake25.setVisibility(View.VISIBLE); Snake25InPlay = true; break;
            case 21: mImgSnake26.setVisibility(View.VISIBLE); Snake26InPlay = true; break;
            case 22: mImgSnake27.setVisibility(View.VISIBLE); Snake27InPlay = true; break;
            case 23: mImgSnake28.setVisibility(View.VISIBLE); Snake28InPlay = true; break;
            case 24: mImgSnake29.setVisibility(View.VISIBLE); Snake29InPlay = true; break;
            case 25: mImgSnake30.setVisibility(View.VISIBLE); Snake30InPlay = true; break;
            case 26: mImgSnake31.setVisibility(View.VISIBLE); Snake31InPlay = true; break;
            case 27: mImgSnake32.setVisibility(View.VISIBLE); Snake32InPlay = true; break;
            case 28: mImgSnake33.setVisibility(View.VISIBLE); Snake33InPlay = true; break;
            case 29: mImgSnake34.setVisibility(View.VISIBLE); Snake34InPlay = true; break;
            case 30: mImgSnake35.setVisibility(View.VISIBLE); Snake35InPlay = true; break;
            case 31: mImgSnake36.setVisibility(View.VISIBLE); Snake36InPlay = true; break;
            case 32: mImgSnake37.setVisibility(View.VISIBLE); Snake37InPlay = true; break;
            case 33: mImgSnake38.setVisibility(View.VISIBLE); Snake38InPlay = true; break;
            case 34: mImgSnake39.setVisibility(View.VISIBLE); Snake39InPlay = true; break;
            case 35: mImgSnake40.setVisibility(View.VISIBLE); Snake40InPlay = true; break;
            case 36: mImgSnake41.setVisibility(View.VISIBLE); Snake41InPlay = true; break;
            case 37: mImgSnake42.setVisibility(View.VISIBLE); Snake42InPlay = true; break;
            case 38: mImgSnake43.setVisibility(View.VISIBLE); Snake43InPlay = true; break;
            case 39: mImgSnake44.setVisibility(View.VISIBLE); Snake44InPlay = true; break;
            case 40: mImgSnake45.setVisibility(View.VISIBLE); Snake45InPlay = true; break;
            case 41: mImgSnake46.setVisibility(View.VISIBLE); Snake46InPlay = true; break;
            case 42: mImgSnake47.setVisibility(View.VISIBLE); Snake47InPlay = true; break;
            case 43: mImgSnake48.setVisibility(View.VISIBLE); Snake48InPlay = true; break;
            case 44: mImgSnake49.setVisibility(View.VISIBLE); Snake49InPlay = true; break;
            case 45: mImgSnake50.setVisibility(View.VISIBLE); Snake50InPlay = true; break;
            case 46: mImgSnake51.setVisibility(View.VISIBLE); Snake51InPlay = true; break;
            case 47: mImgSnake52.setVisibility(View.VISIBLE); Snake52InPlay = true; break;
            case 48: mImgSnake53.setVisibility(View.VISIBLE); Snake53InPlay = true; break;
            case 49: mImgSnake54.setVisibility(View.VISIBLE); Snake54InPlay = true; break;
            case 50: mImgSnake55.setVisibility(View.VISIBLE); Snake55InPlay = true; break;
            case 51: mImgSnake56.setVisibility(View.VISIBLE); Snake56InPlay = true; break;
            case 52: mImgSnake57.setVisibility(View.VISIBLE); Snake57InPlay = true; break;
            case 53: mImgSnake58.setVisibility(View.VISIBLE); Snake58InPlay = true; break;
            case 54: mImgSnake59.setVisibility(View.VISIBLE); Snake59InPlay = true; break;
            case 55: mImgSnake60.setVisibility(View.VISIBLE); Snake60InPlay = true; break;
            case 56: mImgSnake61.setVisibility(View.VISIBLE); Snake61InPlay = true; break;
            case 57: mImgSnake62.setVisibility(View.VISIBLE); Snake62InPlay = true; break;
            case 58: mImgSnake63.setVisibility(View.VISIBLE); Snake63InPlay = true; break;
            case 59: mImgSnake64.setVisibility(View.VISIBLE); Snake64InPlay = true; break;
            case 60: mImgSnake65.setVisibility(View.VISIBLE); Snake65InPlay = true; break;
        }
    }


    private void setCurrentScore(int currentScore) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        String strCurrentScore = numberFormat.format(currentScore);

        String text = String.format("%s: %s", getString(R.string.score), strCurrentScore);
        mTxtScore.setText(text);
    }

    private void setBestScore(int bestScore) {
        String strBestScore = "-";

        if (bestScore > 0) {
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            strBestScore = numberFormat.format(bestScore);
        }

        String text = String.format("%s: %s", getString(R.string.best), strBestScore);
        mTxtBest.setText(text);
    }

    //
    // APPACTIVITY METHODS
    //

    @Override
    void loadView() {

        //
        // load view
        //

        mTxtScore = (TextView) findViewById(R.id.txt_score);
        mTxtBest = (TextView) findViewById(R.id.txt_best);
        mBtPlayPause = (ImageButton) findViewById(R.id.bt_play_pause);

        mVwCenter = findViewById(R.id.vw_center);
        mImgFood = (ImageView) findViewById(R.id.img_food);
        mImgSnake01 = (ImageView) findViewById(R.id.img_snake_01);
        mImgSnake02 = (ImageView) findViewById(R.id.img_snake_02);
        mImgSnake03 = (ImageView) findViewById(R.id.img_snake_03);
        mImgSnake04 = (ImageView) findViewById(R.id.img_snake_04);
        mImgSnake05 = (ImageView) findViewById(R.id.img_snake_05);
        mImgSnake06 = (ImageView) findViewById(R.id.img_snake_06);
        mImgSnake07 = (ImageView) findViewById(R.id.img_snake_07);
        mImgSnake08 = (ImageView) findViewById(R.id.img_snake_08);
        mImgSnake09 = (ImageView) findViewById(R.id.img_snake_09);
        mImgSnake10 = (ImageView) findViewById(R.id.img_snake_10);
        mImgSnake11 = (ImageView) findViewById(R.id.img_snake_11);
        mImgSnake12 = (ImageView) findViewById(R.id.img_snake_12);
        mImgSnake13 = (ImageView) findViewById(R.id.img_snake_13);
        mImgSnake14 = (ImageView) findViewById(R.id.img_snake_14);
        mImgSnake15 = (ImageView) findViewById(R.id.img_snake_15);
        mImgSnake16 = (ImageView) findViewById(R.id.img_snake_16);
        mImgSnake17 = (ImageView) findViewById(R.id.img_snake_17);
        mImgSnake18 = (ImageView) findViewById(R.id.img_snake_18);
        mImgSnake19 = (ImageView) findViewById(R.id.img_snake_19);
        mImgSnake20 = (ImageView) findViewById(R.id.img_snake_20);
        mImgSnake21 = (ImageView) findViewById(R.id.img_snake_21);
        mImgSnake22 = (ImageView) findViewById(R.id.img_snake_22);
        mImgSnake23 = (ImageView) findViewById(R.id.img_snake_23);
        mImgSnake24 = (ImageView) findViewById(R.id.img_snake_24);
        mImgSnake25 = (ImageView) findViewById(R.id.img_snake_25);
        mImgSnake26 = (ImageView) findViewById(R.id.img_snake_26);
        mImgSnake27 = (ImageView) findViewById(R.id.img_snake_27);
        mImgSnake28 = (ImageView) findViewById(R.id.img_snake_28);
        mImgSnake29 = (ImageView) findViewById(R.id.img_snake_29);
        mImgSnake30 = (ImageView) findViewById(R.id.img_snake_30);
        mImgSnake31 = (ImageView) findViewById(R.id.img_snake_31);
        mImgSnake32 = (ImageView) findViewById(R.id.img_snake_32);
        mImgSnake33 = (ImageView) findViewById(R.id.img_snake_33);
        mImgSnake34 = (ImageView) findViewById(R.id.img_snake_34);
        mImgSnake35 = (ImageView) findViewById(R.id.img_snake_35);
        mImgSnake36 = (ImageView) findViewById(R.id.img_snake_36);
        mImgSnake37 = (ImageView) findViewById(R.id.img_snake_37);
        mImgSnake38 = (ImageView) findViewById(R.id.img_snake_38);
        mImgSnake39 = (ImageView) findViewById(R.id.img_snake_39);
        mImgSnake40 = (ImageView) findViewById(R.id.img_snake_40);
        mImgSnake41 = (ImageView) findViewById(R.id.img_snake_41);
        mImgSnake42 = (ImageView) findViewById(R.id.img_snake_42);
        mImgSnake43 = (ImageView) findViewById(R.id.img_snake_43);
        mImgSnake44 = (ImageView) findViewById(R.id.img_snake_44);
        mImgSnake45 = (ImageView) findViewById(R.id.img_snake_45);
        mImgSnake46 = (ImageView) findViewById(R.id.img_snake_46);
        mImgSnake47 = (ImageView) findViewById(R.id.img_snake_47);
        mImgSnake48 = (ImageView) findViewById(R.id.img_snake_48);
        mImgSnake49 = (ImageView) findViewById(R.id.img_snake_49);
        mImgSnake50 = (ImageView) findViewById(R.id.img_snake_50);
        mImgSnake51 = (ImageView) findViewById(R.id.img_snake_51);
        mImgSnake52 = (ImageView) findViewById(R.id.img_snake_52);
        mImgSnake53 = (ImageView) findViewById(R.id.img_snake_53);
        mImgSnake54 = (ImageView) findViewById(R.id.img_snake_54);
        mImgSnake55 = (ImageView) findViewById(R.id.img_snake_55);
        mImgSnake56 = (ImageView) findViewById(R.id.img_snake_56);
        mImgSnake57 = (ImageView) findViewById(R.id.img_snake_57);
        mImgSnake58 = (ImageView) findViewById(R.id.img_snake_58);
        mImgSnake59 = (ImageView) findViewById(R.id.img_snake_59);
        mImgSnake60 = (ImageView) findViewById(R.id.img_snake_60);
        mImgSnake61 = (ImageView) findViewById(R.id.img_snake_61);
        mImgSnake62 = (ImageView) findViewById(R.id.img_snake_62);
        mImgSnake63 = (ImageView) findViewById(R.id.img_snake_63);
        mImgSnake64 = (ImageView) findViewById(R.id.img_snake_64);
        mImgSnake65 = (ImageView) findViewById(R.id.img_snake_65);

        mBtFooter = (Button) findViewById(R.id.bt_footer);
        mLlGuide = (LinearLayout) findViewById(R.id.ll_guide);
        mTxtVersion = (TextView) findViewById(R.id.txt_version);

        //
        // setup listerners
        //

        mBtFooter.setOnClickListener(mBtFooterListerner);
        mBtPlayPause.setOnClickListener(mBtPlayPauseListener);

        ViewTreeObserver viewTreeObserver = mImgSnake01.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(mImgSnake01Listener);

        mGestureDetector = new GestureDetectorCompat(this, mSwipeListener);
    }

    @Override
    void viewDidLoad() {

        //
        // update values
        //

        String score = String.format("%s: %d", getString(R.string.score), 0);
        mTxtScore.setText(score);

        String best = String.format("%s: %d", getString(R.string.best), 0);
        mTxtBest.setText(best);

        //
        // current version
        //

        String versionName = BuildConfig.VERSION_NAME;

        String version = String.format("%s%s", getString(R.string.version), versionName);
        mTxtVersion.setText(version);

        //
        // change center view height
        //

        Display display = getWindowManager().getDefaultDisplay();

        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mVwCenter.getLayoutParams();
        params.height = metrics.widthPixels;
        mVwCenter.setLayoutParams(params);

        //
        // scores
        //

        setCurrentScore(0);

        int bestScore = Preferences.getBestScore(this);
        setBestScore(bestScore);
    }

    //
    // LISTERNERS
    //

    private OnSwipeListener mSwipeListener = new OnSwipeListener() {
        @Override
        public boolean onSwipe(Direction direction) {
            if (IsPlaying) {
                switch (direction) {
                    case UP:
                        moveUp();
                        IsSideMovement = false;
                        break;

                    case DOWN:
                        moveDown();
                        IsSideMovement = false;
                        break;

                    case LEFT:
                        moveLeft();
                        IsSideMovement = true;
                        break;

                    case RIGHT:
                        moveRight();
                        IsSideMovement = true;
                        break;
                }
            }

            return super.onSwipe(direction);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (IsPlaying) {
                pause();
            } else {
                play();
            }

            return super.onDoubleTap(e);
        }
    };

    private ViewTreeObserver.OnGlobalLayoutListener mImgSnake01Listener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            StepSize = mImgSnake01.getHeight();
            X = StepSize;

            ViewTreeObserver observer = mImgSnake01.getViewTreeObserver();

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                observer.removeGlobalOnLayoutListener(this);
            } else {
                observer.removeOnGlobalLayoutListener(this);
            }
        }
    };

    private Button.OnClickListener mBtPlayPauseListener = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (IsPlaying) {
                pause();
            } else {
                play();
            }
        }
    };

    private Button.OnClickListener mBtFooterListerner = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (IsPlaying) {
                pause();
            } else {
                if (IsFirstPlay) {
                    start();
                    IsFirstPlay = false;
                } else {
                    play();
                }
            }
        }
    };

}