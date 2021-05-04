package client.engine;

public class UpdateThread extends Thread 
{
		private int updateRate;
		private InputHandler callback;
		private boolean quitThread;
		
		public UpdateThread(InputHandler callback, int updateRate)
		{
			this.callback = callback;
			this.updateRate = updateRate;
		}
		
		@Override
		public void run()
		{
			double timeFPS0 = System.currentTimeMillis();
			
			while(!quitThread)
			{
				double time = System.currentTimeMillis();
				double deltaTimeFPS = time - timeFPS0;
				
				if(deltaTimeFPS >= updateRate)
				{
					callback.update();
					timeFPS0 = time;
				}
			}
		}

		public void kill() 
		{
			quitThread = true;
		}
}
