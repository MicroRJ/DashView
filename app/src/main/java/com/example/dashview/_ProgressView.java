
package com.example.dashview;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

// NOTE(RJ):
// The following is a simple, segmented progress view.
//
public class _ProgressView extends View
{ public _ProgressView(Context context)
  { super(context);
    Init();
  }
  public _ProgressView(Context context, @Nullable AttributeSet attrs)
  { super(context, attrs);
    Init();
  }
  public _ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
  { super(context, attrs, defStyleAttr);
    Init();
  }
  public _ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes)
  { super(context, attrs, defStyleAttr, defStyleRes);
    Init();
  }
  private final Paint ThisPaint = new Paint(ANTI_ALIAS_FLAG);
  private final RectF ThisRect  = new RectF();
  public volatile float Progress = 0.0f;
  public int ViewWidth = 0;
  public int ViewHeight = 0;
  public int CellWidth = 0;
  public int CellHeight = 0;
  public int SegmentSpacingX = 0;
  public int CellRoundness = 0;
  public int ColorCellActive = 0xFF99B9AE;
  public int ColorCellInactive = 0xFF54605C;
  private float Cells[] = null;
  private void Init()
  { ThisPaint.setStrokeWidth(5);
  }
  @Override
  protected void onSizeChanged(int NewWidth, int NewHeight, int OldWidth, int OldHeight)
  { super.onSizeChanged(NewWidth, NewHeight, OldWidth, OldHeight);
    // NOTE(RJ):
    // Don't initialize anything until get valid dimensions.
    if (NewWidth != 0 && NewHeight != 0)
    { ViewWidth  = NewWidth;
      ViewHeight = NewHeight;
      CellWidth  = 32;
      CellHeight = ViewHeight;
      SegmentSpacingX = 10;
      CellRoundness   = 0;
      int MaxCellCount = Math.round((float) ViewWidth / (CellWidth + SegmentSpacingX));
      Cells = new float[MaxCellCount];
    }
  }
  private int CellIndexToPixels(int Index)
  { return Index*CellWidth + Index*SegmentSpacingX;
  }
  private float Clamp(float Value)
  { return Math.max(0, Math.min(1, Value));
  }
  private void DrawCell(Canvas Surface, int Index, float Progress, int Color)
  { ThisPaint.setColor(Color);
    int CellX = CellIndexToPixels(Index);
    int CellY = 0;
    ThisRect.set(CellX, CellY, CellX + CellWidth, CellY + CellHeight);
    Surface.drawRoundRect(ThisRect, CellRoundness, CellRoundness, ThisPaint);
  }
  private void DrawCells(Canvas Surface, float Progress)
  { float TargetLength  = ViewWidth * Progress;
    int   TargetIndex   = (int) (TargetLength / (CellWidth + SegmentSpacingX));
    for (int CellIndex = 0; CellIndex < Cells.length; CellIndex++)
    { int CellColor = CellIndex < TargetIndex ? ColorCellActive : ColorCellInactive;
      DrawCell(Surface, CellIndex, 1, CellColor);
    }
  }
  @Override
  protected void onDraw(Canvas Surface)
  { super.onDraw(Surface);
    DrawCells(Surface, Progress);
  }
}


//package com.example.dashview;
//
//import static android.graphics.Paint.ANTI_ALIAS_FLAG;
//
//import static java.lang.Math.PI;
//import static java.lang.Math.cos;
//import static java.lang.Math.sin;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Matrix;
//import android.graphics.Paint;
//import android.graphics.RectF;
//import android.util.AttributeSet;
//import android.view.View;
//
//import androidx.annotation.Nullable;
//
//public class _ProgressView extends View
//{
//
//    public _ProgressView(Context context) {
//        super(context);
//    }
//
//    public _ProgressView(Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//        Init();
//    }
//
//    public _ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        Init();
//    }
//
//    public _ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        Init();
//    }
//
//
//    private final Paint ThisPaint = new Paint(ANTI_ALIAS_FLAG);
//    private final RectF ThisRect  = new RectF();
//    public volatile float Progress = 0.0f;
//    public int ViewWidth           = 0;
//    public int ViewHeight          = 0;
//    public int CenterX;
//    public int CenterY;
//
//    public int CellWidth = 0;
//    public int CellHeight = 0;
//    public int SegmentSpacingX     = 0;
//    public int CellRoundness = 0;
//    public int ColorCellActive     = 0xFF99B9AE;
//    public int ColorCellInactive   = 0xFF54605C;
//
//    private float Cells[] = null;
//
//    private void Init()
//    {
//        ThisPaint.setStrokeWidth(5);
//    }
//
//    @Override
//    protected void onSizeChanged(int NewWidth, int NewHeight, int OldWidth, int OldHeight) {
//        super.onSizeChanged(NewWidth, NewHeight, OldWidth, OldHeight);
//        if (NewWidth != 0)
//        {
//            ViewWidth  = NewWidth;
//            ViewHeight = NewHeight;
//            CenterX    = ViewWidth  / 2;
//            CenterY    = ViewHeight / 2;
//
//            CellWidth = 32;
//            CellHeight = ViewHeight;
//            SegmentSpacingX = 10;
//            CellRoundness = 0;
//
//            int MaxCellCount = Math.round((float) ViewWidth / (CellWidth + SegmentSpacingX));
//            Cells = new float[MaxCellCount];
//        }
//    }
//
//
//    private void DrawDebugVerticalLine(Canvas Surface, float x, int Color)
//    {
//        ThisPaint.setColor(Color);
//        Surface.drawLine(x, 0, x, ViewHeight, ThisPaint);
//    }
//
//    private int CellIndexToPixels(int Index)
//    {
//        int p = CellWidth * Index;
//        int s = Index * SegmentSpacingX;
//        return p + s;
//    }
//
//
//    private float Clamp(float Value)
//    {
//        return Math.max(0, Math.min(1, Value));
//    }
//
//    private void DrawCell(Canvas Surface, int Index, float Progress, int Color)
//    {
//        ThisPaint.setColor(Color);
//        int CellX = CellIndexToPixels(Index);
//        int CellY = 0;
//        ThisRect.set(CellX, CellY, CellX + CellWidth, CellY + CellHeight);
//        Surface.drawRoundRect(ThisRect, CellRoundness, CellRoundness, ThisPaint);
//    }
//
//    private void DrawCells(Canvas Surface, float Progress)
//    {
//        float TargetLength  = ViewWidth * Progress;
//        int   TargetIndex   = (int) (TargetLength / (CellWidth + SegmentSpacingX));
//        for (int CellIndex = 0; CellIndex < Cells.length; CellIndex++)
//        {
//            int CellColor = CellIndex < TargetIndex ? ColorCellActive : ColorCellInactive;
//            DrawCell(Surface, CellIndex, 1, CellColor);
////            ThisPaint.setColor(CellColor);
////            int CellX = CellIndexToPixels(CellIndex);
////            int CellY = 0;
////            ThisRect.set(CellX, CellY, CellX + CellWidth, CellY + CellHeight);
////            Surface.drawRoundRect(ThisRect, CellRoundness, CellRoundness, ThisPaint);
//
////            float RelativeDiscrepancy = TargetLength - CellX;
////            if (RelativeDiscrepancy >= 0 && RelativeDiscrepancy <= CellWidth)
////            {
////                float th = Clamp(RelativeDiscrepancy / CellWidth);
////                float h  = CellY + (CellHeight - (CellHeight*th));
////
////                ThisPaint.setColor(0xFF00FF00);
////
////                ThisRect.set(CellX, CellY, CellX + CellWidth, h);
////                Surface.drawRoundRect(ThisRect, CellRoundness, CellRoundness, ThisPaint);
////            }
//        }
//    }
//
//    private static float ToRadiansComplete(float Normal)
//    {
//        assert Normal >= 0;
//        assert Normal <= 1;
//        return (float) (Normal * (PI * 2.f));
//    }
//
//    private static float ToDegreesComplete(float Radians)
//    {
//        return (float) ((Radians / PI) * 180.f);
//    }
//
//    private static final class v3
//    {
//        public float x, y, z;
//    }
//
//    private static final class m3
//    {
//        public float[] m = new float[3*3];
//    }
//
//    private final Matrix SurfaceMatrix = new Matrix();
//
//    private final float[][] Matrix =
//    {
//        {1,     0, 0},
//        {0,     1, 0},
//        {500, 500, 1},
//    };
//
//    private final float[] MatrixRowMajor = new float[9];
//
//    private static float[]
//    ToRowMajor(float[] Matrix, float[] Result)
//    {
//        // Input:
//        // 1, 4, 7,
//        // 2, 5, 8,
//        // 3, 6, 9,
//
//        // Output:
//        // 1, 2, 3,
//        // 4, 5, 6,
//        // 7, 8, 9,
//
//        Result[0] = Matrix[0];
//        Result[1] = Matrix[3];
//        Result[2] = Matrix[6];
//
//        Result[3] = Matrix[1];
//        Result[4] = Matrix[4];
//        Result[5] = Matrix[7];
//
//        Result[6] = Matrix[2];
//        Result[7] = Matrix[5];
//        Result[8] = Matrix[8];
//
//        return Result;
//    }
//
//    private static float[]
//    MatrixRotationX(float v)
//    {
//        return new float[]
//        {
//            (float)  cos(v), (float) sin(v), 0,
//            (float) -sin(v), (float) cos(v), 0,
//            0, 0, 1,
//        };
//    }
//
//    private static float[]
//    MatrixTranslation(float x, float y)
//    {
//        return new float[]
//        {
//            1, 0, 0,
//            0, 1, 0,
//            x, y, 1,
//        };
//    }
//
//    private static float[]
//    MatrixMult(float[] m1, float[] m2)
//    {
//        float[] m = new float[3*3];
//        for (int r = 0; r < 3; r++)
//            for (int c = 0; c < 3; c++)
//                m.m[r][c] = (m1.m[r][0]*m2.m[0][c]) + (m1.m[r][1]*m2.m[1][c]) + (m1.m[r][2]*m2.m[2][c]) + (m1.m[r][3]*m2.m[3][c]);
//            return m;
//    }
//
//
//
//    @Override
//    protected void onDraw(Canvas Surface) {
//        super.onDraw(Surface);
//
//        float[] RotationMatrix = MatrixRotationX((float) (Progress*PI));
//
//        ToRowMajor(RotationMatrix, MatrixRowMajor);
//
//        SurfaceMatrix.setValues(MatrixRowMajor);
//        Surface.setMatrix(SurfaceMatrix);
//
//        ThisPaint.setColor(0xFFFFFFFF);
//        ThisRect.set(0, 0, 200, 200);
//        Surface.drawRect(ThisRect, ThisPaint);
//
////        DrawCells(Surface, Progress);
//
//
////        int Padding = 34;
////        int Rad     = 100; // Math.min(ViewWidth, ViewHeight) / 2 - Padding;
////
////        {
////            float AngleDegrees = 360.f * (1.f - Progress);
////            Surface.save();
////            ThisPaint.setColor(ColorCellActive);
////            ThisRect.set(0, 0, 10, 300);
////            Surface.translate(CenterX, CenterY);
////            Surface.translate(CenterX + Rad + 5, CenterX + Rad + 150);
////            Surface.rotate(AngleDegrees);
////            Surface.drawRect(ThisRect, ThisPaint);
////            Surface.restore();
////        }
//
////        Surface.translate(CenterX, CenterY);
////
////        int CellCount = 128;
////        int CellCountActive = (int) (CellCount*Progress);
////        for (int CellIndex = 0; CellIndex < CellCount; CellIndex++)
////        {
////            float AngleRadians = ToRadiansComplete((float) CellIndex / CellCount);
////            float x = (float) Math.sin(AngleRadians)*Rad;
////            float y = (float) Math.cos(AngleRadians)*Rad;
////
////
////            if (CellIndex <= CellCountActive)
////            {
////                ThisPaint.setColor(ColorCellActive);
////            }
////            else
////            {
////                ThisPaint.setColor(ColorCellInactive);
////            }
////
////            ThisRect.set(x, y, x + 20, y + 40);
////
////            float AngleDegrees = ToDegreesComplete(AngleRadians);
////
////            Surface.save();
////            Surface.rotate(AngleDegrees);
////            Surface.drawRect(ThisRect, ThisPaint);
////            Surface.restore();
////        }
//
//    }
//
//
//}
