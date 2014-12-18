// ActionScript file
//---------------------
// references en as3

var a:Object = new Object();
var b:Object = new Object();
a.x = "a";
b.x = "b";

trace("init");
trace("a : " + a.x); // a : a
trace("b : " + b.x); // b : b
trace("---")

var s_a:Object;
s_a = a; // s_a pointe sur a

trace("s_a = a");
trace("a : " + a.x); // a : a
trace("b : " + b.x); // b : b
trace("s_a : " + s_a.x); // s_a : a
trace("---")

s_a.x = 3; // donc si on modifie s_a, on modifie a aussi

trace("s_a.x = 3;");
trace("a : " + a.x); // a : 3
trace("b : " + b.x); // b : b
trace("s_a : " + s_a.x); // s_a : 3
trace("---")

a = b; // on pointe a sur b : on perd la reference de s_a sur a

trace("a = b;");
trace("a : " + a.x); // a : b
trace("b : " + b.x); // b : b
trace("s_a : " + s_a.x); // s_a : 3
trace("---")

s_a.x = 4; // s_a ne pointe plus sur a

trace("s_a.x = 4;");
trace("a : " + a.x); // a : b
trace("b : " + b.x); // b : b
trace("s_a : " + s_a.x); // s_a : 4
trace("---")


s_a = a; // on pointe de nouveau s_a sur a

trace("s_a.x = a;");
trace("a : " + a.x); // a : b
trace("b : " + b.x); // b : b
trace("s_a : " + s_a.x); // s_a : b
trace("---")

s_a.x = 5; // maintenant s_a pointe sur a et donc aussi sur b !

trace("s_a.x = 5;");
trace("a : " + a.x); // a : 5
trace("b : " + b.x); // b : 5
trace("s_a : " + s_a.x); // s_a : 5
trace("---")

//---------------------