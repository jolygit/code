#include <stdio.h>

struct animal_vtable {
    const char* (*sound)();
};

struct animal {
    const char* (*sound)();
  //    struct animal_vtable methods;
    const char* name;
};

const char* cat_sound() {
    return "meow!";
}

const char* dog_sound() {
    return "bark!";
}

void describe(struct animal *a) {
    printf("%s makes \"%s\" sound.\n", a->name, a->sound());
}

struct animal cat = {&cat_sound, "cat"};
struct animal dog = {&dog_sound, "dog"};

int main() {
    describe(&cat);
    describe(&dog);

    return 0;
}
