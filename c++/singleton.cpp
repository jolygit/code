#include <iostream>
#include <string>
#include <stdlib.h>
using namespace std;

class Number
{
  public:
    // 2. Define a public static accessor func
    static Number *instance(); 
    static void setType(string t)
    {
      type = t;
      delete inst;
      inst = 0;
    }
    virtual void setValue(int in)
    {
      value = in;
    }
    virtual int getValue()
    {
      return value;
    }
  protected:
    int value;
    // 4. Define all ctors to be protected
    Number()
    {
        cout << ":ctor: ";
    } 
  // 1. Define a private static attribute
  private:
    static string type;
    static Number *inst; 
};

string Number::type = "decimal";
Number *Number::inst = 0;

class Octal: public Number
{
  // 6. Inheritance can be supported
  public:
    friend class Number;
    void setValue(int in)
    {
      char buf[10];
      sprintf(buf, "%o", in);
      sscanf(buf, "%d", &value);
    }
  protected:
    Octal(){}
};

Number *Number::instance()
{
  if (!inst)
    // 3. Do "lazy initialization" in the accessor function
    if (type == "octal")
      inst = new Octal();
    else
      inst = new Number();
  return inst;
}

class singleton{
  singleton(){
    cout << ":ctor: ";
  }
  static singleton* ptr;
  int value;
public:
  void set(int v){value=v;}
  int get(){return value;}
  static singleton* instance();
};
singleton* singleton::ptr=0;

singleton* singleton::instance(){
    if (!ptr)
      ptr= new singleton();
    return ptr;
}
int main()
{
  singleton::instance()->set(10);
  cout << singleton::instance()->get()<< endl;
  // Number  myInstance; - error: cannot access protected constructor
  // 5. Clients may only use the accessor function to manipulate the Singleton
  Number::instance()->setValue(42);
  cout << "value is " << Number::instance()->getValue() << endl;
  Number::setType("octal");
  Number::instance()->setValue(64);
  cout << "value is " << Number::instance()->getValue() << endl;
}
