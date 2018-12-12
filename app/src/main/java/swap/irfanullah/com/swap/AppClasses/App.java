package swap.irfanullah.com.swap.AppClasses;

import android.app.Application;
import com.beardedhen.androidbootstrap.TypefaceProvider;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceProvider.registerDefaultIconSets();

    }
}
