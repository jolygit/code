#include <stdio.h>
#include <typeinfo>
struct base;
struct base_vtable
{
  void (*dance)(struct base*); //the pointer to base is necessery to be able to access data members of the derived class
};

struct base
{
    struct base_vtable *vtable;
    /* base members */
};

void base_dance(struct base *d)
{
    printf("base dance\n");
}


struct base_vtable b_vtable =
{
  &base_dance, /* you might get a warning here about incompatible pointer types */
};

void base_init(struct base *d)
{
    d->vtable = &b_vtable;
    /* init base members d->super.foo */
    /* init derived1 members d->foo */
}
struct derived1
{
    struct base super;
    /* derived1 members */
  int _d;
};

void derived1_dance(struct derived1 *d)
{
  printf("derived1 dance%d\n",d->_d);
    /* implementation of derived1's dance function */
}
/* global vtable for derived1 */
struct base_vtable derived1_vtable =
{
  (void (*)(base*))&derived1_dance, /* you might get a warning here about incompatible pointer types */
};

void derived1_init(struct derived1 *d,int dd)
{
    d->super.vtable = &derived1_vtable;
    d->_d=dd;
    /* init base members d->super.foo */
    /* init derived1 members d->foo */
}

namespace kki{
    int kk=0;
}

int main(void)
{
  
  printf("%d",kk);
    /* OK!  We're done with our declarations, now we can finally do some
       polymorphism in C */
    struct derived1 d;
    derived1_init(&d,4);
    struct base *d_ptr = (struct base *)&d;
    d_ptr->vtable->dance(d_ptr);

    struct base b;
    base_init(&b);
    struct base *b_ptr = &b;
    b_ptr->vtable->dance(b_ptr);
    return 0;
}

