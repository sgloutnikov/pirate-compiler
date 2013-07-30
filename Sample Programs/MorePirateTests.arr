
YoHoHo Test$ // program header

/* variable declarations */
Buccaneers
integer intPrecedence$
real realPrecedence$
integer setMe$
integer getMe = 200$
integer aInt$
real aReal$

integer complexMath$
real complexRealMath$

Pillage setter(integer set)
{
	setMe = set$
}

Gunner integer getter()
{
	Overboard getMe$
}

/* main procedure */
Pillage WeighAnchor() 
{
	/*At least two data types with type checking.*/
	aInt = 45$
	aReal = 2.0$

	aReal = aInt / aReal$
	HarHarHar("When applying the mathematical call to 45 / 2.0")$
	HarHarHar("the type is check and converted to real rather than integer")$
	HarHarHar(aReal)$
	HarHarHar("")$
	
	/* Basic arithmetic operations with operator precedence. */
	
	intPrecedence = 1 + 2 * 3 / 4 - 5$ 
	realPrecedence = 6.8 - 3.8 + 2 * 7.5 / 7.5$
	
	HarHarHar("Precedence results to : ")$
	HarHarHar(intPrecedence)$
	HarHarHar("")$
	HarHarHar("Precedence Real results to : ")$
	HarHarHar(realPrecedence)$
	HarHarHar("")$
	
	Arrr(intPrecedence == 11) // If Construct
	{
		HarHarHar("Entered If")$
	}
	TharSheBlows // Else Construct
	{
		HarHarHar("Entered Else")$
	}$
	
	setter(100)$ // This is a procedural call
	HarHarHar(setMe)$
	setMe = getter()$ // This is a function call
	HarHarHar(setMe)$
}