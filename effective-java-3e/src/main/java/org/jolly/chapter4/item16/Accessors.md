## Item 16 - use accessor methods

- if a class is accessible outside its package, provide accessor methods to preserve the flexibility to change the class's internal representation
- if a class is package-private or is a private nested class, there is nothing inherently wrong with exposing its data fields
- it is less harmful if the fields are immutable
