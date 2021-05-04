package server.environment;

public class UpdateThread extends Thread 
{
		private int updateRate;
		private EnvironmentHandler callback;
		private boolean quitThread;
		
		public UpdateThread(EnvironmentHandler callback, int updateRate)
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
				
				if(deltaTime>= updateRate)
				{
					callback.updateEnvironment();
					time0 = time;
				}
			}
		}

		public void kill() 
		{
			quitThread = true;
		}
}
