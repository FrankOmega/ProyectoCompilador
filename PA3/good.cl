class Main {
 main(): Object {42 };
};

class A {
    b:B <- new B;
    moo():Int{
      b.comer()
    };

};

class B inherits A {
    comer():Int{
      8
    };
};
