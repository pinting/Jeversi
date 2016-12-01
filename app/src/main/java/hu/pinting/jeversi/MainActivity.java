package hu.pinting.jeversi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import hu.pinting.jeversi.sqlite.PersistentDataHelper;

public class MainActivity extends Activity {
    private PersistentDataHelper data;
    private GameView game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        data = new PersistentDataHelper(this);
        data.open();

        game = findViewById(R.id.game_view);
        game.setData(data);
        game.resetController();

        final Context context = this;
        final RelativeLayout layout = findViewById(R.id.game_layout);

        // Resize the game view to a square and move it in the center
        layout.post(new Runnable() {
            @Override
            public void run() {
                final Rect rect = new Rect();

                layout.getGlobalVisibleRect(rect);

                int width = rect.width();
                int height = rect.height();
                int size = width > height ? height : width;

                ViewGroup.LayoutParams params = layout.getLayoutParams();

                params.height = size;
                params.width = size;

                layout.setLayoutParams(params);

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)game.getLayoutParams();

                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        data.close();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        data.open();
        game.resetController();
    }

    @Override
    protected void onPause() {
        data.close();
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        game.resetController();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear:
                data.clearBoard();
                game.resetController();
                return true;
            case R.id.settings:
                data.clearBoard();
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}