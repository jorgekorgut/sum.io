package server.intelligence;

/*
 * This class updates the state from the bots with a certain rate.
 */

public class UpdateThread extends Thread 
{
		private int updateRate;
		private IntelligenceHandler callback;
		private boolean quitThread;
		
		public UpdateThread(IntelligenceHandler callback, int updateRate)
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
