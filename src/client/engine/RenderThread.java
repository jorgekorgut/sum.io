package client.engine;

public class RenderThread extends Thread 
{
		private ScreenRender callback;
		private int framesPerSecond;
		boolean quitThread = false;
		
		public RenderThread(ScreenRender callback, int framesPerSecond)
		{
			this.callback = callback;
			this.framesPerSecond = framesPerSecond;
		}
		
		@Override	
		public void run()
		{
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

		public void kill() 
		{
			quitThread = true;
		}
}
