YoHoHo MeProg$ // program header

Buccaneers
integer x = 10$
integer y$
integer z$
boolean a = true$
real f = 10.5$


integer b 4$ 			/* bad declaration, missing = */
integer c == 4$ 		/* bad declaration, bad assignment */
blabla var = 5$ 		/* bad declaration and bad type */
boolean aa = truee$ 	/* bad declaration truee */
integer ppp = 10.1$ 	/* type mismatch, 10.1 is not an integer */
double p = 10.1$ 		/* bad type -- pirates will not use double */
boolean abc = trrrrue$ 	/* not a valid boolean */
boolean bcd = fffalse$  /* not a valid boolean */
real r = false$ 		/* cannot assign a boolean to a real variable */
real rr = "dfe"$ 		/* cannot assign a string to a real variable */
real r2 = 10$ 			/* cannot assign an integer to a real variable */
string s3 = "dfdsf$ 	/* missing end " */
string s6 = dasf"$ 		/* missing beginning " */
string s4 = false$ 		/* cannot assign a boolean to a string variable */
string s5 = 100$		/* cannot assign an integer to a string variable */
integer x = 100$		/* variable already declared */


Pillage badFor(boolean aBoolean)
{
	integer whatsUP$
	Forrre RUM = 70 		/* bad initialization of for loop */
	whatsUP = whatsUP + 1$
	WalkThePlank $ 			/* use of undeclared variable */
	x >>= 3$				/* >>= not a valid symbol */
}


Gunner integer drunkCheck(integer aInteger)
{
	real innerVarTest = 100$ //type mismatch
	integer beersConsumed = 20$
	
	Arrr (beersConsumed > 27)
	{
		drunk = true$ //undeclared variable drunk
	}
	TharSheBlows
	{
		drunk = false$ //undeclared variable drunk
	}$
	Overboard 'd$ //bad return statement.
}


Gunner integer reallyDrunkCheck(rumai) // no type for rumai
{
	integer ddd = 10$
	real rr = 5.3$
}

/* begin main */
Pillage WeighAnchor() {

	MakeSail (xx<=y) //Undeclared 'xx' in condition.
	 	z = z + x$ 
	
	MaakeSail (x<=y) //Bad while.
	 	z = z + x$ 
	 	
	MakeSail (x < y)
	{
		z = z + 1$
		Arrr (z > 10)
		{
			WaalkThePlank$ //Bad break.
		}$
	}$	
}
