/*
Midpoint calculator. From two points, the program will compute
the mid point and determine the quadrant that point is in.

Using functions and procedures, if checks, comparisons on reals, 
which we call floats, print line.
*/
YoHoHo MidPointCalculator$

Buccaneers
real p1x$
real p1y$
real p2x$
real p2y$
real mptemp$

// Set point 1 cordinates procedure.
Pillage enterP1(real x, real y)
{
	p1x = x$
	p1y = y$
}
// Set point 2 cordinates procedure.
Pillage enterP2(real x, real y)
{
	p2x = x$
	p2y = y$	
}

// Compute midpoint given 2 locations
Gunner real computeMp(real arg1, real arg2)
{
	mptemp = arg1 + arg2$
	mptemp = mptemp / 2$
	Overboard mptemp$
}

// Begin main
Pillage WeighAnchor() 
{
	real xmp$
	real ymp$
	enterP1(10.0, 20.0)$
	enterP2(-25.0, 12.0)$
	
	xmp = computeMp(p1x, p2x)$
	ymp = computeMp(p1y, p2y)$
	
	HarHarHar("X Coordinate of Mid-Point:")$
	HarHarHar(xmp)$
	HarHarHar("Y Coordinate of Mid-Point:")$
	HarHarHar(ymp)$
	HarHarHar("The Quadrant of the Mid-Point is:")$
	Arrr((xmp > 0) && (ymp > 0))
	{
		HarHarHar("1st Quadrant")$
	}$
	Arrr((xmp < 0) && (ymp > 0))
	{
		HarHarHar("2nd Quadrant")$
	}$
	Arrr((xmp < 0) && (ymp < 0))
	{
		HarHarHar("3rd Quadrant")$
	}$
	
	Arrr((xmp > 0) && (ymp < 0))
	{
		HarHarHar("4th Quadrant")$
	}$
	
}