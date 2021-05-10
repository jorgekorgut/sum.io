package server.inteligence;

public class UpdateThread extends Thread 
{
		private int updateRate;
		private InteligenceHandler callback;
		private boolean quitThread;
		
		public UpdateThread(InteligenceHandler callback, int updateRate)
		{
			this.callback = callback;
			this.updateRate = updateRate;
		}
		
		@Override
		public void run()
		{
			double time0 = System.currentTimeMillis();
			
			while(!quitThread)
			{
				double time = System.currentTimeMillis();
				double deltaTime = time - time0;
				
				if(deltaTime >= updateRate)
				{
					callback.update();
					time0 = time;
				}
			}
		}

		public void kill() 
		{
			quitThread = true;
		}
}
