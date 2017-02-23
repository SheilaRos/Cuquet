package cat.flx.cuquet;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity implements CuquetView.CuquetViewListener, SensorEventListener {
    private CuquetView cuquetView;
    private TextView tvScore;
    //Creo el Sensor y el SensorManager
    private Sensor sensor;
    private SensorManager sensorManager;
    //Creo una ImageView para mostrar si ha perdido
    private ImageView game;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cuquetView = (CuquetView) findViewById(R.id.cuquetView);
        Button btnNewGame = (Button) findViewById(R.id.btnNewGame);
        tvScore = (TextView) findViewById(R.id.tvScore);
        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvScore.setText("0");
                //Indicamos que la imagen que indica que ha perdido no se muestre
                game.setVisibility(View.INVISIBLE);
                cuquetView.newGame();
            }
        });
        cuquetView.setCuquetViewListener(this);
        //Asignamos los sensores del sistema y obtenemos el acelerometro
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(sensor.TYPE_ACCELEROMETER);
        //Enlazo el ImageView a la imagen que he añadido en el layout y le indico que ha de estar oculta
       game = (ImageView) findViewById(R.id.imageView2);
       game.setVisibility(View.INVISIBLE);
    }
    @Override
    public void onResume() {
        super.onResume();
        //Listener del sensor, indicando cada cuanto tiempo se va a ir actualizando, en este caso <=1 segundo (DELAY_NORMAL)
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_A:
                cuquetView.update(0, +10);
                break;
            case KeyEvent.KEYCODE_Q:
                cuquetView.update(0, -10);
                break;
            case KeyEvent.KEYCODE_O:
                cuquetView.update(-10, 0);
                break;
            case KeyEvent.KEYCODE_P:
                cuquetView.update(+10, 0);
                break;
        }
        return super.dispatchKeyEvent(event);
    }
    @Override public void scoreUpdated(View view, int score) {tvScore.setText(Integer.toString(score));}
    @Override //Cuando pierde le indico que la imagen de "Game Over" se muestre
    public void gameLost(View view) { game.setVisibility(View.VISIBLE); }
    @Override
    public void onSensorChanged(SensorEvent event) {
        //Actualizas la vista en funación a la rotación del teléfono
        float x = -event.values[0];
        float y = event.values[1];
        cuquetView.update(x, y);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
