package control.studio.com.control;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
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
    public ImageView botaoExtra;
    private ToggleButton toggleButton;
    private ToggleButton toggleButtonAlarme;
    static String passaMensagem;
    public ImageView Reed1;
    public ImageView Reed2;
    public ImageView Reed3;
    public ImageView Reed4;
    public ImageView Reed5;
    public ImageView Reed6;

    public MqttCallback ClientCallBack = new MqttCallback() {
        @Override
        public void connectionLost(Throwable cause) {
            Log.d(TAG, "Perda de conexão... Reconectando...");
            connectMQTT();
            assinou = false;
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {

            if (topic.equals("eueduardoCorrente")) { // Apresentada graficamente

                passaMensagem = new String(message.getPayload());
                Log.d(TAG, topic + ": " + passaMensagem);
                AtualizaTextoCorrente(passaMensagem);
            }
            if (topic.equals("eueduardoReed1")) { // Apresentada graficamente

                String Reed1 = new String(message.getPayload());

                if (Reed1.equals("um")) {
                    Log.d(TAG, topic + ": " + Reed1 + "Reed1 ativo");
                    ExibeReed(true, 1);
                } else if (Reed1.equals("zero")) {
                    Log.d(TAG, topic + ": " + Reed1 + "Reed1 Desativado");
                    ExibeReed(false, 1);
                }

            }
            if (topic.equals("eueduardoReed2")) { // Apresentada graficamente

                String Reed2 = new String(message.getPayload());
                Log.d(TAG, topic + ": " + Reed2);
                if (Reed2.equals("um")) {
                    ExibeReed(true, 2);
                } else {
                    ExibeReed(false, 2);
                }
            }
            if (topic.equals("eueduardoReed3")) { // Apresentada graficamente

                String Reed3 = new String(message.getPayload());
                Log.d(TAG, topic + ": " + Reed3);
                if (Reed3.equals("um")) {
                    ExibeReed(true, 3);
                } else {
                    ExibeReed(false, 3);
                }
            }
            if (topic.equals("eueduardoReed4")) { // Apresentada graficamente

                String Reed4 = new String(message.getPayload());
                Log.d(TAG, topic + ": " + Reed4);
                if (Reed4.equals("um")) {
                    ExibeReed(true, 4);
                } else {
                    ExibeReed(false, 4);
                }
            }
            if (topic.equals("eueduardoReed5")) { // Apresentada graficamente

                String Reed5 = new String(message.getPayload());
                Log.d(TAG, topic + ": " + Reed5);
                if (Reed5.equals("um")) {
                    ExibeReed(true, 5);
                } else {
                    ExibeReed(false, 5);
                }
            }
            if (topic.equals("eueduardoReed6")) { // Apresentada graficamente

                String Reed6 = new String(message.getPayload());
                Log.d(TAG, topic + ": " + Reed6);
                if (Reed6.equals("um")) {
                    ExibeReed(true, 6);
                } else if(Reed6.equals("zero")) {
                    ExibeReed(false, 6);
                }
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            Log.d(TAG, "Entregue!");
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

        botaoBaixoA = (ImageView) findViewById(R.id.BaixoAid);
        botaoBaixoB = (ImageView) findViewById(R.id.BaixoBid);
        botaoBaixoC = (ImageView) findViewById(R.id.BaixoCid);

        botaoExtra = (ImageView) findViewById(R.id.ExtraPulsoID);

        Reed1 = (ImageView) findViewById(R.id.reedCimaAid);
        Reed2 = (ImageView) findViewById(R.id.reedBaixoAid);
        Reed3 = (ImageView) findViewById(R.id.reedCimaBid);
        Reed4 = (ImageView) findViewById(R.id.reedBaixoBid);
        Reed5 = (ImageView) findViewById(R.id.reedCimaCid);
        Reed6 = (ImageView) findViewById(R.id.reedBaixoCid);

        toggleButton = (ToggleButton) findViewById(R.id.ExtraID);
        toggleButtonAlarme = (ToggleButton) findViewById(R.id.AlarmeToggleID);

        Alarme = (ImageView) findViewById(R.id.AlarmePulsoID);


        botaoCimaA.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {


                    //CHAMAR COMANDO MQTT DE LIGAR
                    Toast.makeText(MainActivity.this, "Ligou", Toast.LENGTH_SHORT).show();
                    MotoresActivity.botaocima("AcL");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //CHAMAR COMANDO MQTT DE DESLIGAR
                    MotoresActivity.botaocima("AcD");
                    Toast.makeText(MainActivity.this, "Desligou", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        botaoCimaB.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {


                    //CHAMAR COMANDO MQTT DE LIGAR
                    Toast.makeText(MainActivity.this, "Ligou", Toast.LENGTH_SHORT).show();
                    MotoresActivity.botaocima("BcL");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //CHAMAR COMANDO MQTT DE DESLIGAR
                    MotoresActivity.botaocima("BcD");
                    Toast.makeText(MainActivity.this, "Desligou", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        botaoCimaC.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {


                    //CHAMAR COMANDO MQTT DE LIGAR
                    Toast.makeText(MainActivity.this, "Ligou", Toast.LENGTH_SHORT).show();
                    MotoresActivity.botaocima("CcL");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //CHAMAR COMANDO MQTT DE DESLIGAR
                    MotoresActivity.botaocima("CcD");
                    Toast.makeText(MainActivity.this, "Desligou", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });


        //------------
        botaoBaixoA.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {


                    //CHAMAR COMANDO MQTT DE LIGAR
                    Toast.makeText(MainActivity.this, "Ligou", Toast.LENGTH_SHORT).show();
                    MotoresActivity.botaocima("AbL");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //CHAMAR COMANDO MQTT DE DESLIGAR
                    MotoresActivity.botaocima("AbD");
                    Toast.makeText(MainActivity.this, "Desligou", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        botaoBaixoB.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {


                    //CHAMAR COMANDO MQTT DE LIGAR
                    Toast.makeText(MainActivity.this, "Ligou", Toast.LENGTH_SHORT).show();
                    MotoresActivity.botaocima("BbL");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //CHAMAR COMANDO MQTT DE DESLIGAR
                    MotoresActivity.botaocima("BbD");
                    Toast.makeText(MainActivity.this, "Desligou", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        botaoBaixoC.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {


                    //CHAMAR COMANDO MQTT DE LIGAR
                    Toast.makeText(MainActivity.this, "Ligou", Toast.LENGTH_SHORT).show();
                    MotoresActivity.botaocima("CbL");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //CHAMAR COMANDO MQTT DE DESLIGAR
                    MotoresActivity.botaocima("CbD");
                    Toast.makeText(MainActivity.this, "Desligou", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });


        botaoExtra.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {


                    //CHAMAR COMANDO MQTT DE LIGAR
                    Toast.makeText(MainActivity.this, "Ligou", Toast.LENGTH_SHORT).show();
                    MotoresActivity.botaocima("ExtL");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //CHAMAR COMANDO MQTT DE DESLIGAR
                    MotoresActivity.botaocima("ExtD");
                    Toast.makeText(MainActivity.this, "Desligou", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isChecked) {
                    Toast.makeText(MainActivity.this, "Ligou", Toast.LENGTH_SHORT).show();
                    MotoresActivity.botaocima("ExtL");
                }
                if (!isChecked) {
                    Toast.makeText(MainActivity.this, "Desligou", Toast.LENGTH_SHORT).show();
                    MotoresActivity.botaocima("ExtD");
                }
            }
        });

        toggleButtonAlarme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isChecked) {
                    Toast.makeText(MainActivity.this, "Ligou", Toast.LENGTH_SHORT).show();
                    MotoresActivity.botaocima("alarmL");
                }
                if (!isChecked) {
                    Toast.makeText(MainActivity.this, "Desligou", Toast.LENGTH_SHORT).show();
                    MotoresActivity.botaocima("alarmD");
                }
            }
        });


        Alarme.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {


                    //CHAMAR COMANDO MQTT DE LIGAR
                    Toast.makeText(MainActivity.this, "Ligou", Toast.LENGTH_SHORT).show();
                    MotoresActivity.botaocima("alarmL");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //CHAMAR COMANDO MQTT DE DESLIGAR
                    MotoresActivity.botaocima("alarmD");
                    Toast.makeText(MainActivity.this, "Desligou", Toast.LENGTH_SHORT).show();
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
    private void initMQTT() {
        client = new MqttAndroidClient(this.getApplicationContext(),
                "tcp://iot.eclipse.org:1883", clientId);
        client.setCallback(ClientCallBack);
    }

    // Inicialização do MQTT e conexão inicial
    public static void connectMQTT() {
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
            if (!client.isConnected()) {
                connectMQTT();
            }
            IMqttToken subTokenN = client.subscribe("eueduardoCorrente", qos);
            subTokenN.setActionCallback(MqttCallBackApp);

            IMqttToken subTokenU = client.subscribe("eueduardoReed1", qos);
            subTokenU.setActionCallback(MqttCallBackApp);
            IMqttToken subTokenB = client.subscribe("eueduardoReed2", qos);
            subTokenB.setActionCallback(MqttCallBackApp);
            IMqttToken subTokenS = client.subscribe("eueduardoReed3", qos);
            subTokenS.setActionCallback(MqttCallBackApp);
            IMqttToken subTokenA = client.subscribe("eueduardoReed4", qos);
            subTokenA.setActionCallback(MqttCallBackApp);
            IMqttToken subTokenG = client.subscribe("eueduardoReed5", qos);
            subTokenG.setActionCallback(MqttCallBackApp);
            IMqttToken subTokenZ = client.subscribe("eueduardoReed6", qos);
            subTokenZ.setActionCallback(MqttCallBackApp);


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

            //Toast.makeText(MainActivity.this, "publicou por mqtt", Toast.LENGTH_SHORT).show();

        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }

    }

    public void AtualizaTextoCorrente(String passaMensagem) {
        texto = (TextView) findViewById(R.id.correnteID);
        texto.setText(MainActivity.passaMensagem);
    }

    public void ExibeReed(boolean estado, int numeroReed) {

        switch (numeroReed) {
            case 1:
                if (estado == true) {
                    Reed1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.redswitch));
                    Log.d(TAG, "Entrou na funcao ExibeReed1 recebendo true do estado");
                    Toast.makeText(MainActivity.this, "Limite Máximo", Toast.LENGTH_SHORT).show();
                } else if (estado == false) {
                    Reed1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.fundotransparente));
                    Log.d(TAG, "Entrou na funcao ExibeReed1 recebendo FALSE do estado");
                }
                break;


            case 2:
                if (estado == true) {
                    Reed2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.redswitch));
                    Log.d(TAG, "Entrou na funcao ExibeReed2 recebendo true do estado");
                    Toast.makeText(MainActivity.this, "Limite Máximo", Toast.LENGTH_SHORT).show();
                } else if (estado == false) {
                    Reed2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.fundotransparente));
                    Log.d(TAG, "Entrou na funcao ExibeReed2 recebendo FALSE do estado");
                }
                break;


            case 3:
                if (estado == true) {
                    Reed3.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.redswitch));
                    Log.d(TAG, "Entrou na funcao ExibeReed3 recebendo true do estado");
                    Toast.makeText(MainActivity.this, "Limite Máximo", Toast.LENGTH_SHORT).show();
                } else if (estado == false) {
                    Reed3.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.fundotransparente));
                    Log.d(TAG, "Entrou na funcao ExibeReed3 recebendo FALSE do estado");
                }
                break;

            case 4:
                if (estado == true) {
                    Reed4.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.redswitch));
                    Log.d(TAG, "Entrou na funcao ExibeReed4 recebendo true do estado");
                    Toast.makeText(MainActivity.this, "Limite Máximo", Toast.LENGTH_SHORT).show();
                } else if (estado == false) {
                    Reed4.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.fundotransparente));
                    Log.d(TAG, "Entrou na funcao ExibeReed4 recebendo FALSE do estado");
                }
                break;

            case 5:
                if (estado == true) {
                    Reed5.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.redswitch));
                    Log.d(TAG, "Entrou na funcao ExibeReed5 recebendo true do estado");
                    Toast.makeText(MainActivity.this, "Limite Máximo", Toast.LENGTH_SHORT).show();
                } else if (estado == false) {
                    Reed5.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.fundotransparente));
                    Log.d(TAG, "Entrou na funcao ExibeReed5 recebendo FALSE do estado");
                }
                break;

            case 6:
                if (estado == true) {
                    Reed6.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.redswitch));
                    Log.d(TAG, "Entrou na funcao ExibeReed6 recebendo true do estado");
                    Toast.makeText(MainActivity.this, "Limite Máximo", Toast.LENGTH_SHORT).show();
                } else if (estado == false) {
                    Reed6.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.fundotransparente));
                    Log.d(TAG, "Entrou na funcao ExibeReed6 recebendo FALSE do estado");
                }
                break;


            default: Log.d(TAG, " Deu problema no Reed");
                break;

        }


    }
}





