package control.studio.com.control;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MotoresActivity extends MainActivity {

    public TextView texto;
    private ImageView botaoScima;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motores);

        texto = (TextView) findViewById(R.id.textoID);
        texto.setText(MainActivity.passaMensagem);

//        Bundle extra = getIntent().getExtras();
//        if(extra != null){
//            String textoPassado = extra.getString("nome");
//            texto.setText(textoPassado);
//        }else{
//            texto.setText("Não chegou nada");
//    }

        botaoScima = (ImageView) findViewById(R.id.ScimaID);
        botaoScima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MotoresActivity.botaocima("VL");
                Toast.makeText(MotoresActivity.this, "Enviou comando MQTT", Toast.LENGTH_LONG).show();

            }
        });
    }
}
