package client.engine;

public class RenderThread extends Thread 
{
		private int framesPerSecond;
		private int cleanRate;
		private ScreenRender callback;
		
		public RenderThread(ScreenRender callback, int framesPerSecond)
		{
			this.callback = callback;
			this.framesPerSecond = framesPerSecond;
		}
		
		@Override	
		public void run()
		{
			boolean quitThread = false;
			double timeFPS0 = System.currentTimeMillis();
			
			while(!quitThread)
			{
				double time = System.currentTimeMillis();
				double deltaTimeFPS = time - timeFPS0;
				
				if(deltaTimeFPS >= 1000/framesPerSecond)
				{
					callback.draw();
					timeFPS0 = time;
				}
				
			}
		}
}
