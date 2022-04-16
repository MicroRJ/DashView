package com.example.dashview;

import static android.view.WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS;
import static com.example.dashview._Forza._ForzaState;
import static com.example.dashview._Forza._ForzaState.ForzaState;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

// TODO(RJ):
// - Themes
// - Only update the properties that changed.
// - Display all properties on a separate modal like view the user can toggle on or off.
public class MainActivity extends AppCompatActivity
{
  private static final int MIN_PORT = 5000;
  private static final int MAX_PORT = 6000;

  // NOTE(RJ):
  // Use null to use your current IP address.
  //
  private static DatagramSocket Connect(String Address, int Port)
  { try
    { if (Address == null)
      { return new DatagramSocket(Port);
      } else
      { return new DatagramSocket(new InetSocketAddress(Address, Port));
      }
    } catch (SocketException Error)
    { Error.printStackTrace();
      return null;
    }
  }
  private static boolean Read(DatagramSocket Socket, byte[] Buffer)
  { DatagramPacket Packet = new DatagramPacket(Buffer, Buffer.length);
    try
    { Socket.setSoTimeout(3000);
      Socket.receive(Packet);
      return true;
    } catch (IOException Error)
    { Error.printStackTrace();
      return false;
    }
  }
  private static void Obliterate(DatagramSocket Socket)
  { if(Socket != null && Socket.isConnected())
    { Socket.close();
    }
  }
  private static DatagramSocket Socket = null;
  private static boolean Running = false;

  @SuppressLint("SetTextI18n")
  @Override
  protected void onCreate(Bundle savedInstanceState)
  { super.onCreate(savedInstanceState);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
    { getWindow().getAttributes().layoutInDisplayCutoutMode = LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS;
    }
    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
      | View.SYSTEM_UI_FLAG_FULLSCREEN
      | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
      | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
      | View.SYSTEM_UI_FLAG_LOW_PROFILE
      | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
      | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    setContentView(R.layout.activity_main);

    _ProgressView RpmProgressView = findViewById(R.id.RpmProgressView);
    TextView AppInfoTextView = findViewById(R.id.AppInfoTextView);
    TextView GearTextView = findViewById(R.id.GearTextView);
    TextView SpeedometerTextView = findViewById(R.id.SpeedometerTextView);
    SpeedometerTextView.setText("...");

    new Thread(() ->
    { final _ForzaState Forza = ForzaState();
      try
      { // NOTE(RJ):
        // I'm not sure if this is the best way to do things?
        for(int Port = MIN_PORT; Port < MAX_PORT; ++Port)
        { Socket = Connect(null, Port);
          if(Socket != null)
          { break;
          }
        }
        Running = Socket != null;

        while (Running)
        { byte[] Buffer = new byte[512];
          if (Read(Socket, Buffer))
          { Forza.Read(Buffer, 0);
            runOnUiThread(() ->
            { SpeedometerTextView.setText(Integer.toString(Forza.GetMPH()));
              GearTextView.setText(Forza.GetGear());
              if (AppInfoTextView.getVisibility() == View.VISIBLE)
              { AppInfoTextView.setVisibility(View.INVISIBLE);
              }
            });
            RpmProgressView.Progress = Forza.GetRPMBias();
            RpmProgressView.postInvalidate();
          } else
          { runOnUiThread(() ->
            { SpeedometerTextView.setText("NaN");
              GearTextView.setText("NaN");
              if (AppInfoTextView.getVisibility() == View.INVISIBLE)
              { AppInfoTextView.setVisibility(View.VISIBLE);
              }
              AppInfoTextView.setText(String.format("Listening on port %s", Socket.getLocalPort()));
            });
          }
        }
        Obliterate(Socket);
      } catch (Exception Anything)
      { Anything.printStackTrace();
      }
    }).start();
  }

  @Override
  protected void onDestroy()
  { Running = false;
    Obliterate(Socket);
    super.onDestroy();
  }
}