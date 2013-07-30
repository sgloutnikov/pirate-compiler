YoHoHo MeProg$
//
Buccaneers
integer x = 15$
integer y = 5$
real r = 20.5$
real r2 = 100.5$
boolean z = true$

Pillage proc1(integer a)
{
	integer whatsUP = 100$  /* initialize local variable */
	integer whatsUP1 = 200$ /* initialize local variable */
	integer whatsUP2 = 300$ /* initialize local variable */
	integer whatsUP3 = 400$ /* initialize local variable */
	boolean hey = false$
	a = a + 2$
	HarHarHar(a)$ /* print parameter value */
}

Pillage proc2(string c) {
	HarHarHar(c)$
}

Gunner integer increment(integer i) {
	i = i + 1$
	Overboard i$
}

// Begin main
Pillage WeighAnchor() 
{
	integer pirate = 7$ /* initialize local variable */
 	boolean b = true$ /* initialize to false */
 	HarHarHar(pirate)$ 
 	HarHarHar(b)$
 	b = true$
 	HarHarHar(b)$
 	b = false$
 	HarHarHar(b)$
  	// b = (1 < 1)$ /* this isn't working; generating a label thinking it's start of if statement */
 	//HarHarHar(b)$

 	/*Fore RUM = 70
	{

	}$*/
	

	proc1(1)$
	proc1(pirate)$
	HarHarHar(pirate)$ /* pass by value */
	proc2("yay!")$
	
	pirate = increment(10)$ /* function call */
	HarHarHar(pirate)$ 
	
}