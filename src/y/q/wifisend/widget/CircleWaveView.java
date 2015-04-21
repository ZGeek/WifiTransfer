package y.q.wifisend.widget;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import y.q.wifisend.R;

public class CircleWaveView extends View implements Runnable {
	private float mWidth;
	private float mHeight;
	
	private float centerX; //圆心X
	private float centerY; //圆心Y
	private float floatRadius; //变化的半径
	private float maxRadius = -1; //圆半径
	private volatile boolean started = false;
	private Paint mLinePaint;
	private Paint mSolidPaint;
	private int waveColor = Color.rgb(0, 200, 200); //颜色
	private int waveInterval = 70; //圆与圆之间的间隔
	private int waveWidth = 2; //圆环宽度 要小于圆与圆之间的间隔
	private boolean centerAlign = true;//居中
	private float bottomMargin = 0;//底部margin
	private boolean fillCircle = true;//是否填充成实心圆环

	private boolean isShowSearchBar = false;
	private Bitmap searchBitmap= null;
	private int roateArg =0;
	
	public CircleWaveView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CircleWaveView(Context context) {
		this(context, null, 0);
	}
	
	public CircleWaveView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}
	
	private void initView() {
		mLinePaint = new Paint();
		mSolidPaint = new Paint();
	}

	private void init() {
		mWidth = getWidth();
		mHeight = getHeight();
		
		mLinePaint.setAntiAlias(true);
		mLinePaint.setStrokeWidth(1.0F);
		mLinePaint.setStyle(Paint.Style.STROKE);
		mLinePaint.setColor(waveColor);
		
		if (fillCircle) {
			mSolidPaint.setStyle(Paint.Style.STROKE);
			mSolidPaint.setStrokeWidth(waveInterval);
			mSolidPaint.setColor(waveColor);
		}
		
		centerX = mWidth / 2.0F;
		if (centerAlign) {
			centerY = (mHeight / 2.0F);
		}
		else {
			centerY = mHeight - bottomMargin;
		}
		
		if (mWidth >= mHeight) {
			maxRadius = mHeight / 2.0F;
		}
		else {
			maxRadius = mWidth / 2.0F;
		}
		
		floatRadius = (maxRadius % waveInterval);
		
		start();
	}

	private void start() {
		if (!started) {
			started = true;
			if(isShowSearchBar && searchBitmap == null)
			{
				searchBitmap = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.search_bar));
			}
			new Thread(this).start();
		}
	}

	private void stop() {
		started = false;
	}

	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		stop();
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (maxRadius <= 0.0F) {
			return;
		}
		float radius = floatRadius % waveInterval;
		while (true) {
			int alpha = (int) (255.0F * (1.0F - radius / maxRadius));
			if (alpha <= 0) {
				break;
			}
			
			if (fillCircle) {
				mSolidPaint.setAlpha(alpha >> 2);
				canvas.drawCircle(centerX, centerY, radius - waveInterval / 2, mSolidPaint);
			}
			mLinePaint.setAlpha(alpha);
			for(int i = waveWidth; i>=0; i--)
			{
				canvas.drawCircle(centerX, centerY, radius - i, mLinePaint);
			}

			radius += waveInterval;
		}
		if(isShowSearchBar)
		{
			Rect rMoon = new Rect(getWidth()/2-searchBitmap.getWidth(),getHeight()/2,getWidth()/2,getHeight()/2+searchBitmap.getHeight());
			canvas.rotate( (roateArg+=5)%360 ,getWidth()/2,getHeight()/2);
			canvas.drawBitmap(searchBitmap, null, rMoon, null);
		}
	}

	public void onWindowFocusChanged(boolean hasWindowFocus) {
		super.onWindowFocusChanged(hasWindowFocus);
		if (hasWindowFocus) {
			init();
		}
		else {
			stop();
		}
	}

	public void run() {
		while (started) {
			floatRadius = 4.0F + floatRadius;
			if (floatRadius > maxRadius) {
				floatRadius = (maxRadius % waveInterval);
			}
			postInvalidate();
			try {
				Thread.sleep(50L);
			} catch (InterruptedException localInterruptedException) {
				localInterruptedException.printStackTrace();
			}
		}
	}

	public void setMaxRadius(float maxRadius) {
		this.maxRadius = maxRadius;
	}

	public void setWaveColor(int waveColor) {
		this.waveColor = waveColor;
	}

	public void setWaveInterval(int waveInterval) {
		this.waveInterval = waveInterval;
	}

	public void setCenterAlign(boolean centerAlign) {
		this.centerAlign = centerAlign;
	}
}
