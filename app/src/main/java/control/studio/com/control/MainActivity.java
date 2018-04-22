package control.studio.com.control;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import java.io.UnsupportedEncodingException;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    // Declarações de Campos EditText
    private EditText UMaxima, UMinima;
    private ProgressBar pB;
    private ImageView iV;
    private TextView uText;
    // Declaração para as mensagens de depuração
    private static final String TAG = MainActivity.class.getSimpleName();
    // Declarações e inicializações para o MQTT
    public String clientId = MqttClient.generateClientId();
    public static MqttAndroidClient client;
    static boolean assinou = false;
    private ImageView botaoMotores;
    private ImageView botaoAlarme;
    public ImageView botaoCimaA;
    public ImageView botaoCimaB;
    public ImageView botaoCimaC;
    public ImageView botaoCimaD;
    public ImageView botaoBaixoA;
    public ImageView botaoBaixoB;
    public ImageView botaoBaixoC;
    public ImageView botaoBaixoD;
    public TextView texto;
    public ImageView Alarme;
    public ImageView Extra;
private ToggleButton toggleButton;
    static String passaMensagem;

    public MqttCallback ClientCallBack = new MqttCallback() {
        @Override
        public void connectionLost(Throwable cause) {
            Log.d(TAG,"Perda de conexão... Reconectando...");
            connectMQTT();
            assinou = false;
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {

            if (topic.equals("/Umidade")) { // Apresentada graficamente

                passaMensagem= new String(message.getPayload());
                Log.d(TAG, topic + ": " + passaMensagem);
                AtualizaTextoCorrente(passaMensagem);


            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            Log.d(TAG,"Entregue!");
        }
    };

    public static IMqttActionListener MqttCallBackApp = new IMqttActionListener() {
        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            Log.d(TAG, "onSuccess");
            if (!assinou) {
                subscribeMQTT();
                assinou = true;
            }
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken,
                              Throwable exception) {
            // The subscription could not be performed, maybe the user was not
            // authorized to subscribe on the specified topic e.g. using wildcards
            Log.d(TAG, "onFailure");

        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initMQTT();
        connectMQTT();
        //startNotifications();

//------------------------------------------------------
//tentativa de clicar um botao que ao clicar manda o comando de ligar, ao solta-lo manda o comando de desligar
        botaoCimaA = (ImageView) findViewById(R.id.CimaAid);
        botaoCimaB = (ImageView) findViewById(R.id.CimaBid);
        botaoCimaC = (ImageView) findViewById(R.id.CimaCid);
        botaoCimaD = (ImageView) findViewById(R.id.CimaDid);
        botaoBaixoA = (ImageView) findViewById(R.id.BaixoAid);
        botaoBaixoB = (ImageView) findViewById(R.id.BaixoBid);
        botaoBaixoC = (ImageView) findViewById(R.id.BaixoCid);
        botaoBaixoD = (ImageView) findViewById(R.id.BaixoDid);
        toggleButton = (ToggleButton) findViewById(R.id.ExtraID);
        Alarme = (ImageView) findViewById(R.id.AlarmeID);



        botaoCimaA.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {


                    //CHAMAR COMANDO MQTT DE LIGAR
                    Toast.makeText(MainActivity.this, "Ligou", Toast.LENGTH_LONG).show();
                    MotoresActivity.botaocima("AcL");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //CHAMAR COMANDO MQTT DE DESLIGAR
                    MotoresActivity.botaocima("AcD");
                    Toast.makeText(MainActivity.this, "Desligou", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

        botaoCimaB.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {


                    //CHAMAR COMANDO MQTT DE LIGAR
                    Toast.makeText(MainActivity.this, "Ligou", Toast.LENGTH_LONG).show();
                    MotoresActivity.botaocima("BcL");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //CHAMAR COMANDO MQTT DE DESLIGAR
                    MotoresActivity.botaocima("BcD");
                    Toast.makeText(MainActivity.this, "Desligou", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

        botaoCimaC.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {


                    //CHAMAR COMANDO MQTT DE LIGAR
                    Toast.makeText(MainActivity.this, "Ligou", Toast.LENGTH_LONG).show();
                    MotoresActivity.botaocima("CcL");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //CHAMAR COMANDO MQTT DE DESLIGAR
                    MotoresActivity.botaocima("CcD");
                    Toast.makeText(MainActivity.this, "Desligou", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

        botaoCimaD.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {


                    //CHAMAR COMANDO MQTT DE LIGAR
                    Toast.makeText(MainActivity.this, "Ligou", Toast.LENGTH_LONG).show();
                    MotoresActivity.botaocima("DcL");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //CHAMAR COMANDO MQTT DE DESLIGAR
                    MotoresActivity.botaocima("DcD");
                    Toast.makeText(MainActivity.this, "Desligou", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

        //------------
        botaoBaixoA.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {


                    //CHAMAR COMANDO MQTT DE LIGAR
                    Toast.makeText(MainActivity.this, "Ligou", Toast.LENGTH_LONG).show();
                    MotoresActivity.botaocima("AbL");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //CHAMAR COMANDO MQTT DE DESLIGAR
                    MotoresActivity.botaocima("AbD");
                    Toast.makeText(MainActivity.this, "Desligou", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

        botaoBaixoB.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {


                    //CHAMAR COMANDO MQTT DE LIGAR
                    Toast.makeText(MainActivity.this, "Ligou", Toast.LENGTH_LONG).show();
                    MotoresActivity.botaocima("BbL");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //CHAMAR COMANDO MQTT DE DESLIGAR
                    MotoresActivity.botaocima("BbD");
                    Toast.makeText(MainActivity.this, "Desligou", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

        botaoBaixoC.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {


                    //CHAMAR COMANDO MQTT DE LIGAR
                    Toast.makeText(MainActivity.this, "Ligou", Toast.LENGTH_LONG).show();
                    MotoresActivity.botaocima("CbL");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //CHAMAR COMANDO MQTT DE DESLIGAR
                    MotoresActivity.botaocima("CbD");
                    Toast.makeText(MainActivity.this, "Desligou", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

        botaoBaixoD.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {


                    //CHAMAR COMANDO MQTT DE LIGAR
                    Toast.makeText(MainActivity.this, "Ligou", Toast.LENGTH_LONG).show();
                    MotoresActivity.botaocima("DbL");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //CHAMAR COMANDO MQTT DE DESLIGAR
                    MotoresActivity.botaocima("DbD");
                    Toast.makeText(MainActivity.this, "Desligou", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if(isChecked){
                    Toast.makeText(MainActivity.this, "Ligou", Toast.LENGTH_LONG).show();
                    MotoresActivity.botaocima("ExtL");
                }
                if(!isChecked){
                    Toast.makeText(MainActivity.this, "Desligou", Toast.LENGTH_LONG).show();
                    MotoresActivity.botaocima("ExtD");
                }
            }
        });

        Alarme.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {


                    //CHAMAR COMANDO MQTT DE LIGAR
                    Toast.makeText(MainActivity.this, "Ligou", Toast.LENGTH_LONG).show();
                    MotoresActivity.botaocima("alarmL");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //CHAMAR COMANDO MQTT DE DESLIGAR
                    MotoresActivity.botaocima("alarmD");
                    Toast.makeText(MainActivity.this, "Desligou", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });


//------------------------------------------------------

//        botaoMotores = (ImageView) findViewById(R.id.motoresID);
//        botaoAlarme = (ImageView) findViewById(R.id.alarmeID);
//
//        botaoMotores.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //startActivity(new Intent(MainActivity.this, MotoresActivity.class));
//
//                Intent intent = new Intent(MainActivity.this, MotoresActivity.class);
//                intent.putExtra("nome", passaMensagem + " Amperes");
//                startActivity(intent);
//            }
//
//        });
//
//        botaoAlarme.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, AlarmeActivity.class));
//            }
//        });

    }


    // Método de incialização do cliente MQTT
    private void initMQTT(){
        client=new MqttAndroidClient(this.getApplicationContext(),
                "tcp://iot.eclipse.org:1883", clientId);
        client.setCallback(ClientCallBack);
    }

    // Inicialização do MQTT e conexão inicial
    public static void connectMQTT(){
        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName("eueduardo");
            options.setPassword("123".toCharArray());
            IMqttToken token = client.connect(options);
            token.setActionCallback(MqttCallBackApp);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    // Cria as classes necessárias para notificações: Intent, PendingIntent e acessa o mNotificationManager
    /*private void startNotifications(){
        Intent resultIntent = new Intent(this, MainActivity.class);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    }*/

    // Assina as mensagens MQTT desejadas
    public static void subscribeMQTT() {
        int qos = 1;
        try {
            if (!client.isConnected()){
                connectMQTT();
            }
            IMqttToken subTokenU = client.subscribe("/Umidade", qos);
            subTokenU.setActionCallback(MqttCallBackApp);
            IMqttToken subTokenB = client.subscribe("/Bomba", qos);
            subTokenB.setActionCallback(MqttCallBackApp);
            IMqttToken subTokenS = client.subscribe("/Solenoide", qos);
            subTokenS.setActionCallback(MqttCallBackApp);
            IMqttToken subTokenN = client.subscribe("/NivelAgua", qos);
            subTokenN.setActionCallback(MqttCallBackApp);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    // Trata o clique do botão, publicando as mensagens
    public static void botaocima(String payload) { // Evento disparado no Clique do botão
        String topic = "eueduardoVentilador";

        byte[] encodedPayload = new byte[0];
        try {
            if (!client.isConnected()) {
                connectMQTT();
            }
            // client.publish("correnteMaxima",new MqttMessage(CMaxima.getText().toString().getBytes("UTF-8")));
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            client.publish(topic, message);

            //Toast.makeText(MainActivity.this, "publicou por mqtt", Toast.LENGTH_LONG).show();

        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }

    }

    public void AtualizaTextoCorrente(String passaMensagem) {
        texto = (TextView) findViewById(R.id.correnteID);
        texto.setText(MainActivity.passaMensagem);
    }




}
