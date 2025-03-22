# Events

In this folder will be all possible events and messages going between Controller and View,\
all will implement Serializable and be marked according to a specific naming convention:

- __Both Way Events__: "u*Name*", with name being the event's name.
- __View to Controller__: "v*Name*" with name being the event's name.
- __Controller to View__: "c*Name*" with name being the event's name.

No event may run an unchecked exception.
Safe for very specific cases which may hinder the ability of the controller to uphold it's other independent functions and services. All of the unchecked exceptions.