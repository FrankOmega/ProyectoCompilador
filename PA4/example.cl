-- Case branches are tested from most specific to most generic.


class Main inherits IO
{
	a:Main;
  main() : Object
  {
    case a  of

				m : Main => false;
				o : Object => 1;
      esac
  };
};
