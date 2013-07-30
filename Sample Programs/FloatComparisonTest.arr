YoHoHo MeProg$

Buccaneers
real x = 9.0$

Pillage WeighAnchor() 
{
	integer y = 9$

	/* None of these should print, except for the last statement */
	
	Arrr(x < 9.0)
	{
		HarHarHar(x)$
	}$
	
	Arrr(x <= 8.9)
	{
		HarHarHar(x)$
	}$
	
	Arrr(x > 9.01)
	{
		HarHarHar(x)$
	}$

	Arrr(x >= 9.001)
	{
		HarHarHar(x)$
	}$	
	
	Arrr(9.0 == 9.01)
	{
		HarHarHar(x)$
	}$	
	
	Arrr(y != 9.0) 		/* y will be converted to float for comparison */
	{
		HarHarHar(x)$
	}$	
	
	Arrr(9 == 9.0) 		/* 9 will be converted to float for comparison */
	{
		HarHarHar("ahoy!")$
	}$	
	
}
