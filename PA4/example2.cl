class Main{
  a:Int <- 2;
  x:B;
  main(): Int{
    x@A.sumar()
  };
};

class A{
  sumar(): Int{
    1+2
  };
  restar(): Int{
    9-3
  };
  multiplicar(): Int{
    5*4
  };
};

class B Inherits A{
  sumar(): Int{
    3+4
  };
};
