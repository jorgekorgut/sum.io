package server.inteligence;

public class UpdateThread extends Thread 
{
		private int updateRate;
		private InteligenceBrain callback;
		private boolean quitThread;
		
		public UpdateThread(InteligenceBrain callback, int updateRate)
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
					callback.updateBotAction();
					time0 = time;
				}
			}
		}

		public void kill() 
		{
			quitThread = true;
		}
}
