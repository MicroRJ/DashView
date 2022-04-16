package com.example.dashview;

import static com.example.dashview._Forza._ForzaProperty.ForzaField;
import static com.example.dashview._Forza._ForzaKind.f32;
import static com.example.dashview._Forza._ForzaKind.i32;
import static com.example.dashview._Forza._ForzaKind.i8;
import static com.example.dashview._Forza._ForzaKind.u16;
import static com.example.dashview._Forza._ForzaKind.u32;
import static com.example.dashview._Forza._ForzaKind.u8;

import android.renderscript.Sampler;

import java.util.HashMap;

// NOTE(RJ):
// This is a port of one the mini projects from VOID.
//
public interface _Forza
{ public enum _ForzaKind
  { f32(4), u32(4), i32(4),
    u16(2),
    i8(1), u8(1);
    public final int Words;
    _ForzaKind(int Words)
    { this.Words = Words;
    }
  }
  public static final class _ForzaValue
  { public final _ForzaKind Kind;
    public final int        Count;
    public Number           Value = 0;
    public _ForzaValue(_ForzaKind Kind, int Count)
    { this.Kind  = Kind;
      this.Count = Count;
    }
  }

  class _ForzaState
  { // NOTE(RJ):
    // Create a new _ForzaState
    //
	public static _ForzaState ForzaState()
	{ _ForzaState Result = new _ForzaState();
	  int I = 0;
	  for (_ForzaProperty Field : FORZA_FIELDS)
	  { Result.Values[I] = new _ForzaValue(Field.Kind, Field.Count);
	    Result.Fields.put(Field.Name, I);
        I++;
	  }
	  return Result;
	}

	public final _ForzaValue[]            Values = new _ForzaValue[FORZA_FIELDS.length];
    public final HashMap<String, Integer> Fields = new HashMap<>();

    public void Read(byte[] Buffer, int Read)
    { for (_ForzaValue Value : Values)
      { switch (Value.Kind)
        { case f32: Value.Value = Parse_F32(Read, Buffer); break;
          case i32: Value.Value = Parse_I32(Read, Buffer); break;
          case u32: Value.Value = Parse_U32(Read, Buffer); break;
          case u16: Value.Value = Parse_U16(Read, Buffer); break;
          case u8:  Value.Value = Parse_U8(Read, Buffer);  break;
          case i8:  Value.Value = Parse_I8(Read, Buffer);  break;
        }
        Read += Value.Kind.Words*Value.Count;
      }
    }
    public Number Get(Number Default, String Name)
    { if (Fields.containsKey(Name))
      { return Values[Fields.get(Name)].Value;
      } else
      { return Default;
      }
    }
    public synchronized int GetMPH()
    { float SpeedMPS = Get(0.f, "Speed").floatValue();
      return (int) ((SpeedMPS * 60.f * 60.f / 1609.344f) + 0.5f);
    }
    public synchronized Number GetKMH()
    { float SpeedMPS = Get(0.f, "Speed").floatValue();
      return (int) ((SpeedMPS * 60.f * 60.f / 1000.f) + 0.5f);
    }
    public synchronized String GetGear()
    { int Gear = Get(0, "Gear").intValue();
      return (Gear == 0 ? "R" : Integer.toString(Gear));
    }
    public synchronized float GetRPM()
    { return Get(0, "engine_rpm_current").floatValue();
    }
    public synchronized float GetRPMMax()
    { return Get(0, "engine_rpm_max").floatValue();
    }
    public synchronized float GetRPMBias()
    { return  GetRPM()/GetRPMMax();
    }
  }
  static class _ForzaProperty
  { static _ForzaProperty ForzaField(_ForzaKind Kind, String Name)
    { return new _ForzaProperty(Kind, 1, Name);
    }
    public static _ForzaProperty ForzaField(_ForzaKind Kind, int Count)
    {  return new _ForzaProperty(Kind, Count, "no-name");
    }
    private _ForzaProperty(_ForzaKind Kind, int Count, String Name)
    { this.Kind  = Kind;
      this.Count = Count;
      this.Name  = Name;
    }
    public final _ForzaKind Kind;
    public final int        Count;
    public final String     Name;
  }
  // NOTE(RJ):
  // These are recycled for all states to use,
  // although, there's only one state ever used!
  //
  static _ForzaProperty[] FORZA_FIELDS =
  { ForzaField(u32, "IsRaceOn"),
    ForzaField(u32, "TimestampMS"),
    ForzaField(f32, "engine_rpm_max"),
    ForzaField(f32, "engine_rpm_idle"),
    ForzaField(f32, "engine_rpm_current"),
    ForzaField(f32, "AccelerationX"),
    ForzaField(f32, "AccelerationY"),
    ForzaField(f32, "AccelerationZ"),
    ForzaField(f32, "VelocityX"),
    ForzaField(f32, "VelocityY"),
    ForzaField(f32, "VelocityZ"),
    ForzaField(f32, "AngularVelocityX"),
    ForzaField(f32, "AngularVelocityY"),
    ForzaField(f32, "AngularVelocityZ"),
    ForzaField(f32, "Yaw"),
    ForzaField(f32, "Pitch"),
    ForzaField(f32, "Roll"),
    ForzaField(f32, "NormalizedSuspensionTravelFrontLeft"),
    ForzaField(f32, "NormalizedSuspensionTravelFrontRight"),
    ForzaField(f32, "NormalizedSuspensionTravelRearLeft"),
    ForzaField(f32, "NormalizedSuspensionTravelRearRight"),
    ForzaField(f32, "TireSlipRatioFrontLeft"),
    ForzaField(f32, "TireSlipRatioFrontRight"),
    ForzaField(f32, "TireSlipRatioRearLeft"),
    ForzaField(f32, "TireSlipRatioRearRight"),
    ForzaField(f32, "WheelRotationSpeedFrontLeft"),
    ForzaField(f32, "WheelRotationSpeedFrontRight"),
    ForzaField(f32, "WheelRotationSpeedRearLeft"),
    ForzaField(f32, "WheelRotationSpeedRearRight"),
    ForzaField(i32, "WheelOnRumbleStripFrontLeft"),
    ForzaField(i32, "WheelOnRumbleStripFrontRight"),
    ForzaField(i32, "WheelOnRumbleStripRearLeft"),
    ForzaField(i32, "WheelOnRumbleStripRearRight"),
    ForzaField(f32, "WheelInPuddleDepthFrontLeft"),
    ForzaField(f32, "WheelInPuddleDepthFrontRight"),
    ForzaField(f32, "WheelInPuddleDepthRearLeft"),
    ForzaField(f32, "WheelInPuddleDepthRearRight"),
    ForzaField(f32, "SurfaceRumbleFrontLeft"),
    ForzaField(f32, "SurfaceRumbleFrontRight"),
    ForzaField(f32, "SurfaceRumbleRearLeft"),
    ForzaField(f32, "SurfaceRumbleRearRight"),
    ForzaField(f32, "TireSlipAngleFrontLeft"),
    ForzaField(f32, "TireSlipAngleFrontRight"),
    ForzaField(f32, "TireSlipAngleRearLeft"),
    ForzaField(f32, "TireSlipAngleRearRight"),
    ForzaField(f32, "TireCombinedSlipFrontLeft"),
    ForzaField(f32, "TireCombinedSlipFrontRight"),
    ForzaField(f32, "TireCombinedSlipRearLeft"),
    ForzaField(f32, "TireCombinedSlipRearRight"),
    ForzaField(f32, "SuspensionTravelMetersFrontLeft"),
    ForzaField(f32, "SuspensionTravelMetersFrontRight"),
    ForzaField(f32, "SuspensionTravelMetersRearLeft"),
    ForzaField(f32, "SuspensionTravelMetersRearRight"),
    ForzaField(i32, "CarOrdinal"),
    ForzaField(i32, "CarClass"),
    ForzaField(i32, "CarPerformanceIndex"),
    ForzaField(i32, "DrivetrainType"),
    ForzaField(i32, "NumCylinders"),
    ForzaField(u8,  12),
    ForzaField(f32, "PositionX"),
    ForzaField(f32, "PositionY"),
    ForzaField(f32, "PositionZ"),
    ForzaField(f32, "Speed"),
    ForzaField(f32, "Power"),
    ForzaField(f32, "Torque"),
    ForzaField(f32, "TireTempFrontLeft"),
    ForzaField(f32, "TireTempFrontRight"),
    ForzaField(f32, "TireTempRearLeft"),
    ForzaField(f32, "TireTempRearRight"),
    ForzaField(f32, "Boost"),
    ForzaField(f32, "Fuel"),
    ForzaField(f32, "DistanceTraveled"),
    ForzaField(f32, "BestLap"),
    ForzaField(f32, "LastLap"),
    ForzaField(f32, "CurrentLap"),
    ForzaField(f32, "CurrentRaceTime"),
    ForzaField(u16, "LapNumber"),
    ForzaField(u8,  "RacePosition"),
    ForzaField(u8,  "Accel"),
    ForzaField(u8,  "Brake"),
    ForzaField(u8,  "Clutch"),
    ForzaField(u8,  "HandBrake"),
    ForzaField(u8,  "Gear"),
    ForzaField(i8,  "Steer"),
  };
  static long PackI64_(int Offset, int Count, byte[] Buffer)
  { long Result = 0;
    for (int Index = 0; Index < Count; Index++)
    { long U = Buffer[Offset + Index] & 0xFFL;
      Result |= U << (Index * 8);
    }
    return Result;
  }
  static byte Pack_I8(int Offset, byte[] Buffer)
  { Requires(Buffer.length > Offset);
    return Buffer[Offset];
  }
  static long Parse_I8(int Offset, byte[] Buffer)
  { Requires(Buffer.length > Offset);
    return Pack_I8(Offset, Buffer);
  }
  static long Parse_U8(int Offset, byte[] Buffer)
  { Requires(Buffer.length > Offset);
    return ((int) Pack_I8(Offset, Buffer)) & 0xFF;
  }
  static long Parse_U16(int Offset, byte[] Buffer)
  { Requires(Buffer.length > Offset);
    return PackI64_(Offset, 2, Buffer) & 0xFFFFFFFFL;
  }
  static long Parse_I32(int Offset, byte[] Buffer)
  { Requires(Buffer.length > Offset);
    return PackI64_(Offset, 4, Buffer) & 0xFFFFFFFFL;
  }
  static long Parse_U32(int Offset, byte[] Buffer)
  { Requires(Buffer.length > Offset);
    return PackI64_(Offset, 4, Buffer) & 0xFFFFFFFFL;
  }
  static float Parse_F32(int Offset, byte[] Buffer)
  { Requires(Buffer.length > Offset);
    int Int = (int) Parse_U32(Offset, Buffer);
    return Float.intBitsToFloat(Int);
  }
  static void Requires(boolean Requirement)
  { if (!Requirement)
    { throw new RuntimeException("Requirement Failed!");
    }
  }
}
