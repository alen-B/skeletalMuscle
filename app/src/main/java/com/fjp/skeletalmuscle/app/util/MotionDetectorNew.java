package com.fjp.skeletalmuscle.app.util;

import java.util.ArrayList;
import java.util.List;

public class MotionDetectorNew {
   // 枚举：动作类型
   public enum MovementType {
      OVERHEAD_RAISE,  // 上举
      CHEST_EXPANSION, // 扩胸
      UNKNOWN
   }
   // 每帧传感器数据（此处假设已融合：pitch, yaw, roll, ax, ay, az）
   // 如果你有左右两个传感器，也可在这里存左右手信息
   public static class SensorFrame {
      public float pitch;   // 俯仰角
      public float yaw;     // 航向角
      public float roll;    // 横滚角
      public float ax;      // x方向加速度
      public float ay;      // y方向加速度
      public float az;      // z方向加速度
      // 可加上角速度: gx, gy, gz (如需要)
      public float gx;
      public float gy;
      public float gz;
      public long timestamp; // 时间戳
      public SensorFrame(
              float pitch, float yaw, float roll,
              float ax, float ay, float az,
              float gx, float gy, float gz,
              long timestamp
      ) {
         this.pitch = pitch;
         this.yaw   = yaw;
         this.roll  = roll;
         this.ax = ax;
         this.ay = ay;
         this.az = az;
         this.gx = gx;
         this.gy = gy;
         this.gz = gz;
         this.timestamp = timestamp;
      }
   }
   // 输出结果：动作类型 + 对应的角度
   public static class MovementResult {
      public MovementType type;
      public float angle; // 用于存上举角度或扩胸角度
      // 你也可以输出更多信息，比如 pitchRange, yawDiff等
      public MovementResult(MovementType t, float ang) {
         this.type = t;
         this.angle = ang;
      }
      @Override
      public String toString() {
         return "MovementResult{" +
                 "type=" + type +
                 ", angle=" + angle +
                 '}';
      }
   }
   /**
    * 核心判断逻辑：给定一段SensorFrame序列，判断动作并计算角度
    * 说明：此处以“单只手的上举”为例，或“单只手的扩胸”为例。
    * 若你有左右手数据，也可进行扩展（计算左右 yaw 差）。
    *
    * @param frames 一段时长的传感器数据
    * @return MovementResult
    */
   public static MovementResult detectMovement(List<SensorFrame> frames) {
      if (frames == null || frames.isEmpty()) {
         return new MovementResult(MovementType.UNKNOWN, 0f);
      }
      // 1) 统计 pitch 的最小值、最大值
      float minPitch = Float.MAX_VALUE;
      float maxPitch = -Float.MAX_VALUE;
      // 2) 统计 yaw 的最小值、最大值
      float minYaw = Float.MAX_VALUE;
      float maxYaw = -Float.MAX_VALUE;
      // 3) 加速度峰值、角速度峰值(可选)
      float maxAccel = 0f;
      float maxGyro  = 0f;
      // 4) 记录最后一帧 pitch,yaw (看动作结束时姿态)
      float finalPitch = 0f;
      float finalYaw   = 0f;
      // 遍历数据
      for (SensorFrame f : frames) {
         // 更新 pitch 范围
         if (f.pitch < minPitch) minPitch = f.pitch;
         if (f.pitch > maxPitch) maxPitch = f.pitch;
         // 更新 yaw 范围
         if (f.yaw < minYaw) minYaw = f.yaw;
         if (f.yaw > maxYaw) maxYaw = f.yaw;
         // 计算加速度模
         float accelMag = (float)Math.sqrt(f.ax * f.ax + f.ay * f.ay + f.az * f.az);
         if (accelMag > maxAccel) maxAccel = accelMag;
         // 计算角速度模(如需要)
         float gyroMag = (float)Math.sqrt(f.gx * f.gx + f.gy * f.gy + f.gz * f.gz);
         if (gyroMag > maxGyro) maxGyro = gyroMag;
         // 记录最后一帧 pitch,yaw
         finalPitch = f.pitch;
         finalYaw   = f.yaw;
      }
      // 计算 pitchRange, yawRange
      float pitchRange = Math.abs(maxPitch - minPitch);
      float yawRange   = Math.abs(maxYaw - minYaw);
      // 设定一些阈值 (示例)
      final float PITCH_MOVE_THRESHOLD = 30f; // pitch变化若>30°,可视为上举幅度较大
      final float ACCEL_THRESHOLD      = 2.0f; // 加速度>2g 表示明显运动
      final float GYRO_THRESHOLD       = 3.0f; // 角速度阈值(示例)
      final float PITCH_UPPER_BOUND    = 100f; // 上举结束时 pitch <~100 视为上举
      // 扩胸时 pitch可能在 170~190 或 0°/360°(看解算)
      // 这里随意设 160f 作为"近水平"判断示例
      final float PITCH_NEAR_HORIZONTAL = 160f;
      // yaw 大幅度范围变化(>30~40等) 可视为水平展开
      final float YAW_EXPAND_THRESHOLD  = 30f;
      // 判断是否“有足够运动”
      boolean isMoving = (pitchRange > PITCH_MOVE_THRESHOLD || yawRange > YAW_EXPAND_THRESHOLD) &&
              (maxAccel > ACCEL_THRESHOLD || maxGyro > GYRO_THRESHOLD);
      // 默认
      MovementType type = MovementType.UNKNOWN;
      float angle = 0f;
      if (isMoving) {
         // 先看 是否满足上举
         // if final pitch < ~100 且 pitchRange大 => Overhead Raise
         if (finalPitch < PITCH_UPPER_BOUND && pitchRange > PITCH_MOVE_THRESHOLD) {
            type = MovementType.OVERHEAD_RAISE;
            // 假设自然下垂 pitch ~ 120°, 上举角度 = 120° - finalPitch
            float NATURAL_PITCH = 120f; // 你的项目中自己定义
            angle = NATURAL_PITCH - finalPitch;
            if (angle < 0) angle = 0; // 避免出现负值
         }
         else {
            // 否则，看扩胸： final pitch > 160°, yaw范围大
            if (finalPitch > PITCH_NEAR_HORIZONTAL && yawRange > YAW_EXPAND_THRESHOLD) {
               type = MovementType.CHEST_EXPANSION;
               // 简单地用 yawRange 作为“扩胸角度”
               angle = yawRange;
            }
         }
      }
      return new MovementResult(type, angle);
   }
   /**
    * 演示如何把“左右手”一起判断扩胸角度。
    * @param leftFrames  左手数据序列
    * @param rightFrames 右手数据序列
    * @return MovementResult
    */
   public static MovementResult detectChestExpansionTwoHands(
           List<SensorFrame> leftFrames,
           List<SensorFrame> rightFrames
   ) {
      // 为简化，此处假设左右手时序相同、frame数相同(实际中需做对齐)
      if (leftFrames == null || leftFrames.isEmpty() || rightFrames == null || rightFrames.isEmpty()) {
         return new MovementResult(MovementType.UNKNOWN, 0f);
      }
      // 先找最后一帧
      SensorFrame leftFinal  = leftFrames.get(leftFrames.size() - 1);
      SensorFrame rightFinal = rightFrames.get(rightFrames.size() - 1);
      // 计算左右 yaw 差
      float yawDiff = Math.abs(leftFinal.yaw - rightFinal.yaw);
      // 判断 pitch 是否接近水平
      // 例如认为 pitch>160 或 pitch<20 => 水平
      final float PITCH_NEAR_HORIZONTAL = 160f;
      boolean leftNearHorizontal  = (leftFinal.pitch > PITCH_NEAR_HORIZONTAL || leftFinal.pitch < 20f);
      boolean rightNearHorizontal = (rightFinal.pitch > PITCH_NEAR_HORIZONTAL || rightFinal.pitch < 20f);
      // 若都 nearHorizontal 且 yawDiff>30 => chest expansion
      if (leftNearHorizontal && rightNearHorizontal && yawDiff > 30f) {
         return new MovementResult(MovementType.CHEST_EXPANSION, yawDiff);
      }
      return new MovementResult(MovementType.UNKNOWN, 0f);
   }
   public static void main(String[] args) {
      // 模拟一段“右手上举”的数据
      List<SensorFrame> rightHandFrames = new ArrayList<>();
      // 这里演示手动添加几帧（pitch约110->90），加速度、角速度略写
      rightHandFrames.add(new SensorFrame(118f, -41.97f, 19.22f, 3.2f, 2.9f, 8.9f, 3.65f, 2.90f, 11.03f, System.currentTimeMillis()));
      rightHandFrames.add(new SensorFrame(109.9f, -56.3f, 17.9f, 3.0f, 1.5f, 8.1f, 2.86f, 4.45f, 7.97f, System.currentTimeMillis()+20));
      rightHandFrames.add(new SensorFrame(106.9f, -64.56f, 15.3f, 1.5f, 0.84f, 8.41f, 1.24f, 2.50f, 4.53f, System.currentTimeMillis()+40));
      rightHandFrames.add(new SensorFrame( 90f, -77f,  6.4f, 0.4f, 0.5f, 7.5f, 1.61f, 0.59f, 0.09f,  System.currentTimeMillis()+60));
      // 调用检测
      MovementResult result = detectMovement(rightHandFrames);
      System.out.println("Right Hand Movement: " + result);
      // 若要演示“左右手扩胸”，可模拟 leftHandFrames + rightHandFrames
      // 并调用 detectChestExpansionTwoHands(leftHandFrames, rightHandFrames)。
   }
}