# Optimser

## Critical issues ignored

The relationship between PostalAddress and CorrespondencePartPerson and CorrespondencePartUnit is a polymorphic
association. A ORM like hibernate is able to handle such an association, but I was not able to get it work given
reasonable time constraints. Therefore, we are leaving the relationship with a CRITICAL warning from the optimiser.

If someone wishes to explore this issue further and solve the hibernate mapping annotations for CorrespondencePart*, I'd
be happy to hear about it.

