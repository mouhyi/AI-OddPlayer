for(int i = 0;i<1000;i++){
  	ServerSocket ss = new ServerSocket(8123);
	OddBoard b = new OddBoard();
	Thread s = new Thread(new Server(b, false, true, ss, 5000));
	s.start();
	c = new Thread(new Client(new AIPlayer(),"localhost",8123));
	c1 = new Thread(new Client(new OddRandomPlayer(),"localhost",8123));
	ArrayList<Thread> clients = new ArrayList<>();
	clients.add(c);
	clients.add(c1);
	Collections.shuffle(clients);
	clients.get(0).start();
	Thread.sleep(500);
	clients.get(1).start();
	s.join();
	clients.get(0).join();
	clients.get(1).join();
	ss.close();
}