package me.furtado.isnake.activity;

import android.app.Activity;

/**
 * Created by bfurtado on 08/01/16.
 */
abstract class AppActivity extends Activity {

    abstract void loadView();

    abstract void viewDidLoad();

}
