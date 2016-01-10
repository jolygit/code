var person1={
    fname: "gigi",
    lname: "joly",
}
var p2 = Object.create(person1);
p2.fullname="gigi joly";
console.log(Object.getPrototypeOf(p2));// base is person1 i.e p2 inherits from person1

// using prototype 

function Car() {}
car1 = new Car();

console.log(car1.color);    // undefined
 
Car.prototype.color = "black"; // color is shared by all Car objects not just one instance like car1, in other words you add property to an existing object by means of abave
console.log(car1.color);    // null

// another way to inherit

/*function Plant () {
​this.country = "Mexico";
​this.isOrganic = true;
}

function Fruit (fruitName, fruitColor) {
​this.name = fruitName;
​this.color = fruitColor;
}

Fruit.prototype = new Plant ();

var aBanana = new Fruit ("Banana", "Yellow");*/

//////
var Person = function() {
  this.canTalk = true;
};
Person.prototype.greet="hello";
/*Person.prototype.greet = function() {
  if (this.canTalk) {
    console.log('Hi, I am ' + this.name);
  }
};*/

var Employee = function(name, title) {
  Person.call(this);
  this.name = name;
  this.title = title;
};

Employee.prototype = Object.create(Person.prototype);
//Employee.prototype.constructor = Employee;
Employee.prototype.greet="hi";
person=new Person();
employee=new Employee("vova","krasik");
p1=new employee.constructor;
console.log(employee.constructor===Person);
console.log(Object.getPrototypeOf(employee));
console.log(p1);

////

function Shape() {
  this.x = 0;
  this.y = 0;
}

// superclass method
Shape.prototype.move = function(x, y) {
  this.x += x;
  this.y += y;
  console.info('Shape moved.');
};

// Rectangle - subclass
function Rectangle() {
  Shape.call(this); // call super constructor.
}
Rectangle.prototype = Object.create(Shape.prototype);// otherwise Rectangle.prototype is empty
Rectangle.prototype.constructor = Rectangle;
rectangle=new Rectangle();
shape=Object.create(Shape);
console.log(shape.x);
