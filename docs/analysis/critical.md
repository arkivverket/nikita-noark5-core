# Problems marked critical

2021-03-14 18:24:42.795 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
TablePerClassInheritanceEvent - The [aud_nikita.common.model.noark5.v5.SystemIdEntity_AUD] entity uses the
InheritanceType.TABLE_PER_CLASS strategy which renders very inefficient SQL statements. For more info about this event,
check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#TablePerClassInheritanceEvent
2021-03-14 18:24:42.817 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.meeting.MeetingParticipant] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:42.821 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
TablePerClassInheritanceEvent - The [aud_nikita.common.model.noark5.v5.ChangeLog_AUD] entity uses the
InheritanceType.TABLE_PER_CLASS strategy which renders very inefficient SQL statements. For more info about this event,
check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#TablePerClassInheritanceEvent
2021-03-14 18:24:42.866 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartUnit] entity requires both ends to be
synchronized. Only the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:42.870 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceBSMBase] bidirectional association in
the [nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartUnit] entity requires both ends to be
synchronized. Only the [addBSMBase(nikita.common.model.noark5.bsm.BSMBase bSMBase)] could be found. Consider adding
the [removeBSMBase(nikita.common.model.noark5.bsm.BSMBase bSMBase)]  synchronization method as well. For more info about
this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:42.872 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceRecord] attribute in
the [nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartUnit] entity uses eager fetching. Consider
using a lazy fetching which, not only that is more efficient, but it is way more flexible when it comes to fetching
data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:42.877 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [businessAddress] attribute in
the [nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartUnit] entity uses eager fetching. Consider
using a lazy fetching which, not only that is more efficient, but it is way more flexible when it comes to fetching
data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:42.878 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
OneToOneParentSideEvent - The [businessAddress] one-to-one association in
the [class nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartUnit] entity is mapped as the
parent-side of this relationship. The parent-side of a one-to-one association is fetched eagerly unless bytecode
enhancement lazy loading is enabled and the association is annotated with @LazyToOne(LazyToOneOption.NO_PROXY). For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#OneToOneParentSideEvent
2021-03-14 18:24:42.880 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [contactInformation] attribute in
the [nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartUnit] entity uses eager fetching. Consider
using a lazy fetching which, not only that is more efficient, but it is way more flexible when it comes to fetching
data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:42.880 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
OneToOneParentSideEvent - The [contactInformation] one-to-one association in
the [class nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartUnit] entity is mapped as the
parent-side of this relationship. The parent-side of a one-to-one association is fetched eagerly unless bytecode
enhancement lazy loading is enabled and the association is annotated with @LazyToOne(LazyToOneOption.NO_PROXY). For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#OneToOneParentSideEvent
2021-03-14 18:24:42.881 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [postalAddress] attribute in
the [nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartUnit] entity uses eager fetching. Consider
using a lazy fetching which, not only that is more efficient, but it is way more flexible when it comes to fetching
data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:42.881 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
OneToOneParentSideEvent - The [postalAddress] one-to-one association in
the [class nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartUnit] entity is mapped as the
parent-side of this relationship. The parent-side of a one-to-one association is fetched eagerly unless bytecode
enhancement lazy loading is enabled and the association is annotated with @LazyToOne(LazyToOneOption.NO_PROXY). For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#OneToOneParentSideEvent
2021-03-14 18:24:42.891 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.ElectronicSignature] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:42.900 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceDocumentObject] attribute in
the [nikita.common.model.noark5.v5.secondary.ElectronicSignature] entity uses eager fetching. Consider using a lazy
fetching which, not only that is more efficient, but it is way more flexible when it comes to fetching data. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:42.901 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceDocumentDescription] attribute in
the [nikita.common.model.noark5.v5.secondary.ElectronicSignature] entity uses eager fetching. Consider using a lazy
fetching which, not only that is more efficient, but it is way more flexible when it comes to fetching data. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:42.922 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.casehandling.RegistryEntry] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:42.931 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceAuthor] bidirectional association in
the [nikita.common.model.noark5.v5.casehandling.RegistryEntry] entity requires both ends to be synchronized. Only
the [addAuthor(nikita.common.model.noark5.v5.secondary.Author author)] could be found. Consider adding
the [removeAuthor(nikita.common.model.noark5.v5.secondary.Author author)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:42.932 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceBSMBase] bidirectional association in
the [nikita.common.model.noark5.v5.casehandling.RegistryEntry] entity requires both ends to be synchronized. Only
the [addBSMBase(nikita.common.model.noark5.bsm.BSMBase bSMBase)] could be found. Consider adding
the [removeBSMBase(nikita.common.model.noark5.bsm.BSMBase bSMBase)]  synchronization method as well. For more info about
this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:42.935 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceClassified] attribute in
the [nikita.common.model.noark5.v5.casehandling.RegistryEntry] entity uses eager fetching. Consider using a lazy
fetching which, not only that is more efficient, but it is way more flexible when it comes to fetching data. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:42.937 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceComment] many-to-many association in
the [nikita.common.model.noark5.v5.casehandling.RegistryEntry] entity is using a List, so it does not render very
efficient SQL statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:42.942 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceCorrespondencePart] bidirectional association in
the [nikita.common.model.noark5.v5.casehandling.RegistryEntry] entity requires both ends to be synchronized. Only
the [addCorrespondencePart(nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePart correspondencePart)]
could be found. Consider adding
the [removeCorrespondencePart(nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePart correspondencePart)]
synchronization method as well. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:42.943 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceCrossReference] bidirectional association in
the [nikita.common.model.noark5.v5.casehandling.RegistryEntry] entity requires both ends to be synchronized. Only
the [addCrossReference(nikita.common.model.noark5.v5.secondary.CrossReference crossReference)] could be found. Consider
adding the [removeCrossReference(nikita.common.model.noark5.v5.secondary.CrossReference crossReference)]
synchronization method as well. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:42.945 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceDisposal] attribute in the [nikita.common.model.noark5.v5.casehandling.RegistryEntry]
entity uses eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more
flexible when it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:42.946 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceDocumentDescription] many-to-many association in
the [nikita.common.model.noark5.v5.casehandling.RegistryEntry] entity is using a List, so it does not render very
efficient SQL statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:42.948 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceKeyword] many-to-many association in
the [nikita.common.model.noark5.v5.casehandling.RegistryEntry] entity is using a List, so it does not render very
efficient SQL statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:42.950 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceNationalIdentifier] bidirectional association in
the [nikita.common.model.noark5.v5.casehandling.RegistryEntry] entity requires both ends to be synchronized. Only
the [addNationalIdentifier(nikita.common.model.noark5.v5.nationalidentifier.NationalIdentifier nationalIdentifier)]
could be found. Consider adding
the [removeNationalIdentifier(nikita.common.model.noark5.v5.nationalidentifier.NationalIdentifier nationalIdentifier)]
synchronization method as well. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:42.951 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referencePart] many-to-many association in
the [nikita.common.model.noark5.v5.casehandling.RegistryEntry] entity is using a List, so it does not render very
efficient SQL statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:42.952 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceScreening] attribute in
the [nikita.common.model.noark5.v5.casehandling.RegistryEntry] entity uses eager fetching. Consider using a lazy
fetching which, not only that is more efficient, but it is way more flexible when it comes to fetching data. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:42.955 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceStorageLocation] many-to-many association in
the [nikita.common.model.noark5.v5.casehandling.RegistryEntry] entity is using a List, so it does not render very
efficient SQL statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:42.969 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceDocumentFlow] bidirectional association in
the [nikita.common.model.noark5.v5.casehandling.RegistryEntry] entity requires both ends to be synchronized. Only
the [addDocumentFlow(nikita.common.model.noark5.v5.secondary.DocumentFlow documentFlow)] could be found. Consider adding
the [removeDocumentFlow(nikita.common.model.noark5.v5.secondary.DocumentFlow documentFlow)]  synchronization method as
well. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:42.970 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referencePrecedence] many-to-many association in
the [nikita.common.model.noark5.v5.casehandling.RegistryEntry] entity is using a List, so it does not render very
efficient SQL statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:42.971 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceSignOff] many-to-many association in
the [nikita.common.model.noark5.v5.casehandling.RegistryEntry] entity is using a List, so it does not render very
efficient SQL statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:42.979 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceElectronicSignature] attribute in
the [nikita.common.model.noark5.v5.casehandling.RegistryEntry] entity uses eager fetching. Consider using a lazy
fetching which, not only that is more efficient, but it is way more flexible when it comes to fetching data. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:42.999 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.PartUnit] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.007 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceBSMBase] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.PartUnit] entity requires both ends to be synchronized. Only
the [addBSMBase(nikita.common.model.noark5.bsm.BSMBase bSMBase)] could be found. Consider adding
the [removeBSMBase(nikita.common.model.noark5.bsm.BSMBase bSMBase)]  synchronization method as well. For more info about
this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.008 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceDocumentDescription] many-to-many association in
the [nikita.common.model.noark5.v5.secondary.PartUnit] entity is using a List, so it does not render very efficient SQL
statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:43.009 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceDocumentDescription] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.PartUnit] entity requires both ends to be synchronized. Only
the [addDocumentDescription(nikita.common.model.noark5.v5.DocumentDescription documentDescription)] could be found.
Consider adding the [removeDocumentDescription(nikita.common.model.noark5.v5.DocumentDescription documentDescription)]
synchronization method as well. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.010 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceFile] many-to-many association in
the [nikita.common.model.noark5.v5.secondary.PartUnit] entity is using a List, so it does not render very efficient SQL
statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:43.010 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceFile] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.PartUnit] entity requires both ends to be synchronized. Only
the [addFile(nikita.common.model.noark5.v5.File file)] could be found. Consider adding
the [removeFile(nikita.common.model.noark5.v5.File file)]  synchronization method as well. For more info about this
event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.012 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceRecord] many-to-many association in
the [nikita.common.model.noark5.v5.secondary.PartUnit] entity is using a List, so it does not render very efficient SQL
statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:43.012 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceRecord] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.PartUnit] entity requires both ends to be synchronized. Only
the [addRecord(nikita.common.model.noark5.v5.Record record)] could be found. Consider adding
the [removeRecord(nikita.common.model.noark5.v5.Record record)]  synchronization method as well. For more info about
this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.016 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [businessAddress] attribute in the [nikita.common.model.noark5.v5.secondary.PartUnit] entity
uses eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more flexible
when it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.017 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
OneToOneParentSideEvent - The [businessAddress] one-to-one association in
the [class nikita.common.model.noark5.v5.secondary.PartUnit] entity is mapped as the parent-side of this relationship.
The parent-side of a one-to-one association is fetched eagerly unless bytecode enhancement lazy loading is enabled and
the association is annotated with @LazyToOne(LazyToOneOption.NO_PROXY). For more info about this event, check out this
User Guide link - https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#OneToOneParentSideEvent
2021-03-14 18:24:43.018 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [contactInformation] attribute in the [nikita.common.model.noark5.v5.secondary.PartUnit] entity
uses eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more flexible
when it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.018 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
OneToOneParentSideEvent - The [contactInformation] one-to-one association in
the [class nikita.common.model.noark5.v5.secondary.PartUnit] entity is mapped as the parent-side of this relationship.
The parent-side of a one-to-one association is fetched eagerly unless bytecode enhancement lazy loading is enabled and
the association is annotated with @LazyToOne(LazyToOneOption.NO_PROXY). For more info about this event, check out this
User Guide link - https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#OneToOneParentSideEvent
2021-03-14 18:24:43.019 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [postalAddress] attribute in the [nikita.common.model.noark5.v5.secondary.PartUnit] entity uses
eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more flexible when
it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.020 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
OneToOneParentSideEvent - The [postalAddress] one-to-one association in
the [class nikita.common.model.noark5.v5.secondary.PartUnit] entity is mapped as the parent-side of this relationship.
The parent-side of a one-to-one association is fetched eagerly unless bytecode enhancement lazy loading is enabled and
the association is annotated with @LazyToOne(LazyToOneOption.NO_PROXY). For more info about this event, check out this
User Guide link - https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#OneToOneParentSideEvent
2021-03-14 18:24:43.030 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.DocumentDescription] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.044 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceAuthor] bidirectional association in
the [nikita.common.model.noark5.v5.DocumentDescription] entity requires both ends to be synchronized. Only
the [addAuthor(nikita.common.model.noark5.v5.secondary.Author author)] could be found. Consider adding
the [removeAuthor(nikita.common.model.noark5.v5.secondary.Author author)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.046 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceClassified] attribute in the [nikita.common.model.noark5.v5.DocumentDescription]
entity uses eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more
flexible when it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.047 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceComment] many-to-many association in
the [nikita.common.model.noark5.v5.DocumentDescription] entity is using a List, so it does not render very efficient SQL
statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:43.048 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceDeletion] attribute in the [nikita.common.model.noark5.v5.DocumentDescription] entity
uses eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more flexible
when it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.049 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceDisposal] attribute in the [nikita.common.model.noark5.v5.DocumentDescription] entity
uses eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more flexible
when it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.050 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceDisposalUndertaken] attribute in
the [nikita.common.model.noark5.v5.DocumentDescription] entity uses eager fetching. Consider using a lazy fetching
which, not only that is more efficient, but it is way more flexible when it comes to fetching data. For more info about
this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.051 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceDocumentObject] bidirectional association in
the [nikita.common.model.noark5.v5.DocumentDescription] entity requires both ends to be synchronized. Only
the [addDocumentObject(nikita.common.model.noark5.v5.DocumentObject documentObject)] could be found. Consider adding
the [removeDocumentObject(nikita.common.model.noark5.v5.DocumentObject documentObject)]  synchronization method as well.
For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.053 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referencePart] many-to-many association in
the [nikita.common.model.noark5.v5.DocumentDescription] entity is using a List, so it does not render very efficient SQL
statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:43.054 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceRecord] many-to-many association in
the [nikita.common.model.noark5.v5.DocumentDescription] entity is using a List, so it does not render very efficient SQL
statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:43.054 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceRecord] bidirectional association in
the [nikita.common.model.noark5.v5.DocumentDescription] entity requires both ends to be synchronized. Only
the [addRecord(nikita.common.model.noark5.v5.Record record)] could be found. Consider adding
the [removeRecord(nikita.common.model.noark5.v5.Record record)]  synchronization method as well. For more info about
this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.055 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceScreening] attribute in the [nikita.common.model.noark5.v5.DocumentDescription]
entity uses eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more
flexible when it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.059 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceElectronicSignature] attribute in
the [nikita.common.model.noark5.v5.DocumentDescription] entity uses eager fetching. Consider using a lazy fetching
which, not only that is more efficient, but it is way more flexible when it comes to fetching data. For more info about
this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.128 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.EventLog] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.135 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceSystemIdEntity] attribute in the [nikita.common.model.noark5.v5.EventLog] entity uses
eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more flexible when
it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.164 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.casehandling.RecordNote] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.176 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceAuthor] bidirectional association in
the [nikita.common.model.noark5.v5.casehandling.RecordNote] entity requires both ends to be synchronized. Only
the [addAuthor(nikita.common.model.noark5.v5.secondary.Author author)] could be found. Consider adding
the [removeAuthor(nikita.common.model.noark5.v5.secondary.Author author)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.178 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceBSMBase] bidirectional association in
the [nikita.common.model.noark5.v5.casehandling.RecordNote] entity requires both ends to be synchronized. Only
the [addBSMBase(nikita.common.model.noark5.bsm.BSMBase bSMBase)] could be found. Consider adding
the [removeBSMBase(nikita.common.model.noark5.bsm.BSMBase bSMBase)]  synchronization method as well. For more info about
this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.182 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceClassified] attribute in the [nikita.common.model.noark5.v5.casehandling.RecordNote]
entity uses eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more
flexible when it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.183 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceComment] many-to-many association in
the [nikita.common.model.noark5.v5.casehandling.RecordNote] entity is using a List, so it does not render very efficient
SQL statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:43.185 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceCorrespondencePart] bidirectional association in
the [nikita.common.model.noark5.v5.casehandling.RecordNote] entity requires both ends to be synchronized. Only
the [addCorrespondencePart(nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePart correspondencePart)]
could be found. Consider adding
the [removeCorrespondencePart(nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePart correspondencePart)]
synchronization method as well. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.186 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceCrossReference] bidirectional association in
the [nikita.common.model.noark5.v5.casehandling.RecordNote] entity requires both ends to be synchronized. Only
the [addCrossReference(nikita.common.model.noark5.v5.secondary.CrossReference crossReference)] could be found. Consider
adding the [removeCrossReference(nikita.common.model.noark5.v5.secondary.CrossReference crossReference)]
synchronization method as well. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.187 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceDisposal] attribute in the [nikita.common.model.noark5.v5.casehandling.RecordNote]
entity uses eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more
flexible when it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.188 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceDocumentDescription] many-to-many association in
the [nikita.common.model.noark5.v5.casehandling.RecordNote] entity is using a List, so it does not render very efficient
SQL statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:43.192 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceKeyword] many-to-many association in
the [nikita.common.model.noark5.v5.casehandling.RecordNote] entity is using a List, so it does not render very efficient
SQL statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:43.194 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceNationalIdentifier] bidirectional association in
the [nikita.common.model.noark5.v5.casehandling.RecordNote] entity requires both ends to be synchronized. Only
the [addNationalIdentifier(nikita.common.model.noark5.v5.nationalidentifier.NationalIdentifier nationalIdentifier)]
could be found. Consider adding
the [removeNationalIdentifier(nikita.common.model.noark5.v5.nationalidentifier.NationalIdentifier nationalIdentifier)]
synchronization method as well. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.196 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referencePart] many-to-many association in
the [nikita.common.model.noark5.v5.casehandling.RecordNote] entity is using a List, so it does not render very efficient
SQL statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:43.198 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceScreening] attribute in the [nikita.common.model.noark5.v5.casehandling.RecordNote]
entity uses eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more
flexible when it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.201 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceStorageLocation] many-to-many association in
the [nikita.common.model.noark5.v5.casehandling.RecordNote] entity is using a List, so it does not render very efficient
SQL statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:43.217 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceDocumentFlow] bidirectional association in
the [nikita.common.model.noark5.v5.casehandling.RecordNote] entity requires both ends to be synchronized. Only
the [addDocumentFlow(nikita.common.model.noark5.v5.secondary.DocumentFlow documentFlow)] could be found. Consider adding
the [removeDocumentFlow(nikita.common.model.noark5.v5.secondary.DocumentFlow documentFlow)]  synchronization method as
well. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.257 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.Classified] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.264 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceClass] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.Classified] entity requires both ends to be synchronized. Consider adding
the [addClass(nikita.common.model.noark5.v5.Class class)] and [removeClass(nikita.common.model.noark5.v5.Class class)]
synchronization methods. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.266 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceDocumentDescription] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.Classified] entity requires both ends to be synchronized. Consider adding
the [addDocumentDescription(nikita.common.model.noark5.v5.DocumentDescription documentDescription)]
and [removeDocumentDescription(nikita.common.model.noark5.v5.DocumentDescription documentDescription)] synchronization
methods. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.267 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceFile] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.Classified] entity requires both ends to be synchronized. Consider adding
the [addFile(nikita.common.model.noark5.v5.File file)] and [removeFile(nikita.common.model.noark5.v5.File file)]
synchronization methods. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.268 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceRecord] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.Classified] entity requires both ends to be synchronized. Consider adding
the [addRecord(nikita.common.model.noark5.v5.Record record)]
and [removeRecord(nikita.common.model.noark5.v5.Record record)] synchronization methods. For more info about this event,
check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.269 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceSeries] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.Classified] entity requires both ends to be synchronized. Consider adding
the [addSeries(nikita.common.model.noark5.v5.Series series)]
and [removeSeries(nikita.common.model.noark5.v5.Series series)] synchronization methods. For more info about this event,
check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.301 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceAdministrativeUnit] attribute in the [nikita.common.model.noark5.bsm.BSMBase] entity
uses eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more flexible
when it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.302 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceCorrespondencePart] attribute in the [nikita.common.model.noark5.bsm.BSMBase] entity
uses eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more flexible
when it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.303 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceFile] attribute in the [nikita.common.model.noark5.bsm.BSMBase] entity uses eager
fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more flexible when it
comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.304 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referencePart] attribute in the [nikita.common.model.noark5.bsm.BSMBase] entity uses eager
fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more flexible when it
comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.305 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceRecord] attribute in the [nikita.common.model.noark5.bsm.BSMBase] entity uses eager
fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more flexible when it
comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.318 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.casehandling.CaseFile] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.327 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceBSMBase] bidirectional association in
the [nikita.common.model.noark5.v5.casehandling.CaseFile] entity requires both ends to be synchronized. Only
the [addBSMBase(nikita.common.model.noark5.bsm.BSMBase bSMBase)] could be found. Consider adding
the [removeBSMBase(nikita.common.model.noark5.bsm.BSMBase bSMBase)]  synchronization method as well. For more info about
this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.329 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChildFile] bidirectional association in
the [nikita.common.model.noark5.v5.casehandling.CaseFile] entity requires both ends to be synchronized. Consider adding
the [addFile(nikita.common.model.noark5.v5.File file)] and [removeFile(nikita.common.model.noark5.v5.File file)]
synchronization methods. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.331 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceClassified] attribute in the [nikita.common.model.noark5.v5.casehandling.CaseFile]
entity uses eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more
flexible when it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.332 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceComment] many-to-many association in
the [nikita.common.model.noark5.v5.casehandling.CaseFile] entity is using a List, so it does not render very efficient
SQL statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:43.333 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceCrossReference] bidirectional association in
the [nikita.common.model.noark5.v5.casehandling.CaseFile] entity requires both ends to be synchronized. Only
the [addCrossReference(nikita.common.model.noark5.v5.secondary.CrossReference crossReference)] could be found. Consider
adding the [removeCrossReference(nikita.common.model.noark5.v5.secondary.CrossReference crossReference)]
synchronization method as well. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.334 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceDisposal] attribute in the [nikita.common.model.noark5.v5.casehandling.CaseFile]
entity uses eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more
flexible when it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.335 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceKeyword] many-to-many association in
the [nikita.common.model.noark5.v5.casehandling.CaseFile] entity is using a List, so it does not render very efficient
SQL statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:43.337 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceNationalIdentifier] bidirectional association in
the [nikita.common.model.noark5.v5.casehandling.CaseFile] entity requires both ends to be synchronized. Only
the [addNationalIdentifier(nikita.common.model.noark5.v5.nationalidentifier.NationalIdentifier nationalIdentifier)]
could be found. Consider adding
the [removeNationalIdentifier(nikita.common.model.noark5.v5.nationalidentifier.NationalIdentifier nationalIdentifier)]
synchronization method as well. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.339 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referencePart] many-to-many association in
the [nikita.common.model.noark5.v5.casehandling.CaseFile] entity is using a List, so it does not render very efficient
SQL statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:43.340 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceRecord] bidirectional association in
the [nikita.common.model.noark5.v5.casehandling.CaseFile] entity requires both ends to be synchronized. Consider adding
the [addRecord(nikita.common.model.noark5.v5.Record record)]
and [removeRecord(nikita.common.model.noark5.v5.Record record)] synchronization methods. For more info about this event,
check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.341 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceScreening] attribute in the [nikita.common.model.noark5.v5.casehandling.CaseFile]
entity uses eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more
flexible when it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.344 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceStorageLocation] many-to-many association in
the [nikita.common.model.noark5.v5.casehandling.CaseFile] entity is using a List, so it does not render very efficient
SQL statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:43.354 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceAdministrativeUnit] attribute in
the [nikita.common.model.noark5.v5.casehandling.CaseFile] entity uses eager fetching. Consider using a lazy fetching
which, not only that is more efficient, but it is way more flexible when it comes to fetching data. For more info about
this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.355 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referencePrecedence] many-to-many association in
the [nikita.common.model.noark5.v5.casehandling.CaseFile] entity is using a List, so it does not render very efficient
SQL statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:43.355 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
TablePerClassInheritanceEvent - The [aud_nikita.common.model.noark5.v5.File_AUD] entity uses the
InheritanceType.TABLE_PER_CLASS strategy which renders very inefficient SQL statements. For more info about this event,
check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#TablePerClassInheritanceEvent
2021-03-14 18:24:43.369 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
TablePerClassInheritanceEvent - The [nikita.common.model.noark5.v5.Record] entity uses the
InheritanceType.TABLE_PER_CLASS strategy which renders very inefficient SQL statements. For more info about this event,
check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#TablePerClassInheritanceEvent
2021-03-14 18:24:43.378 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.Record] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.392 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceAuthor] bidirectional association in
the [nikita.common.model.noark5.v5.Record] entity requires both ends to be synchronized. Only
the [addAuthor(nikita.common.model.noark5.v5.secondary.Author author)] could be found. Consider adding
the [removeAuthor(nikita.common.model.noark5.v5.secondary.Author author)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.394 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceBSMBase] bidirectional association in
the [nikita.common.model.noark5.v5.Record] entity requires both ends to be synchronized. Only
the [addBSMBase(nikita.common.model.noark5.bsm.BSMBase bSMBase)] could be found. Consider adding
the [removeBSMBase(nikita.common.model.noark5.bsm.BSMBase bSMBase)]  synchronization method as well. For more info about
this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.396 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceClassified] attribute in the [nikita.common.model.noark5.v5.Record] entity uses eager
fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more flexible when it
comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.397 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceComment] many-to-many association in the [nikita.common.model.noark5.v5.Record]
entity is using a List, so it does not render very efficient SQL statements. Consider using a Set instead. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:43.398 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceCorrespondencePart] bidirectional association in
the [nikita.common.model.noark5.v5.Record] entity requires both ends to be synchronized. Only
the [addCorrespondencePart(nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePart correspondencePart)]
could be found. Consider adding
the [removeCorrespondencePart(nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePart correspondencePart)]
synchronization method as well. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.400 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceCrossReference] bidirectional association in
the [nikita.common.model.noark5.v5.Record] entity requires both ends to be synchronized. Only
the [addCrossReference(nikita.common.model.noark5.v5.secondary.CrossReference crossReference)] could be found. Consider
adding the [removeCrossReference(nikita.common.model.noark5.v5.secondary.CrossReference crossReference)]
synchronization method as well. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.401 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceDisposal] attribute in the [nikita.common.model.noark5.v5.Record] entity uses eager
fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more flexible when it
comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.403 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceDocumentDescription] many-to-many association in
the [nikita.common.model.noark5.v5.Record] entity is using a List, so it does not render very efficient SQL statements.
Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:43.405 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceKeyword] many-to-many association in the [nikita.common.model.noark5.v5.Record]
entity is using a List, so it does not render very efficient SQL statements. Consider using a Set instead. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:43.406 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceNationalIdentifier] bidirectional association in
the [nikita.common.model.noark5.v5.Record] entity requires both ends to be synchronized. Only
the [addNationalIdentifier(nikita.common.model.noark5.v5.nationalidentifier.NationalIdentifier nationalIdentifier)]
could be found. Consider adding
the [removeNationalIdentifier(nikita.common.model.noark5.v5.nationalidentifier.NationalIdentifier nationalIdentifier)]
synchronization method as well. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.407 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referencePart] many-to-many association in the [nikita.common.model.noark5.v5.Record] entity
is using a List, so it does not render very efficient SQL statements. Consider using a Set instead. For more info about
this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:43.408 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceScreening] attribute in the [nikita.common.model.noark5.v5.Record] entity uses eager
fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more flexible when it
comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.411 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceStorageLocation] many-to-many association in
the [nikita.common.model.noark5.v5.Record] entity is using a List, so it does not render very efficient SQL statements.
Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:43.414 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
TableGeneratorEvent - The [id] identifier attribute in the [nikita.common.model.noark5.v5.admin.Authority] entity uses
the TABLE strategy, which does not scale very well. Consider using the IDENTITY identifier strategy instead, even if it
does not allow JDBC batch inserts. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#TableGeneratorEvent
2021-03-14 18:24:43.417 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [users] many-to-many association in the [nikita.common.model.noark5.v5.admin.Authority] entity
is using a List, so it does not render very efficient SQL statements. Consider using a Set instead. For more info about
this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:43.417 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [users] bidirectional association in
the [nikita.common.model.noark5.v5.admin.Authority] entity requires both ends to be synchronized. Consider adding
the [addUser(nikita.common.model.noark5.v5.admin.User user)]
and [removeUser(nikita.common.model.noark5.v5.admin.User user)] synchronization methods. For more info about this event,
check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.458 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.Keyword] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.460 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceClass] many-to-many association in
the [nikita.common.model.noark5.v5.secondary.Keyword] entity is using a List, so it does not render very efficient SQL
statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:43.461 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceClass] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.Keyword] entity requires both ends to be synchronized. Consider adding
the [addClass(nikita.common.model.noark5.v5.Class class)] and [removeClass(nikita.common.model.noark5.v5.Class class)]
synchronization methods. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.462 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceFile] many-to-many association in
the [nikita.common.model.noark5.v5.secondary.Keyword] entity is using a List, so it does not render very efficient SQL
statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:43.462 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceFile] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.Keyword] entity requires both ends to be synchronized. Consider adding
the [addFile(nikita.common.model.noark5.v5.File file)] and [removeFile(nikita.common.model.noark5.v5.File file)]
synchronization methods. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.463 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceRecord] many-to-many association in
the [nikita.common.model.noark5.v5.secondary.Keyword] entity is using a List, so it does not render very efficient SQL
statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:43.463 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceRecord] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.Keyword] entity requires both ends to be synchronized. Consider adding
the [addRecord(nikita.common.model.noark5.v5.Record record)]
and [removeRecord(nikita.common.model.noark5.v5.Record record)] synchronization methods. For more info about this event,
check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.507 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
TablePerClassInheritanceEvent - The [nikita.common.model.noark5.v5.ChangeLog] entity uses the
InheritanceType.TABLE_PER_CLASS strategy which renders very inefficient SQL statements. For more info about this event,
check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#TablePerClassInheritanceEvent
2021-03-14 18:24:43.515 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.ChangeLog] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.522 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceSystemIdEntity] attribute in the [nikita.common.model.noark5.v5.ChangeLog] entity
uses eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more flexible
when it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.545 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.Series] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.552 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceClassificationSystem] many-to-many association in
the [nikita.common.model.noark5.v5.Series] entity is using a List, so it does not render very efficient SQL statements.
Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:43.554 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceClassified] attribute in the [nikita.common.model.noark5.v5.Series] entity uses eager
fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more flexible when it
comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.555 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceDeletion] attribute in the [nikita.common.model.noark5.v5.Series] entity uses eager
fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more flexible when it
comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.556 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceDisposal] attribute in the [nikita.common.model.noark5.v5.Series] entity uses eager
fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more flexible when it
comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.557 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceDisposalUndertaken] attribute in the [nikita.common.model.noark5.v5.Series] entity
uses eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more flexible
when it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.558 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceFile] bidirectional association in
the [nikita.common.model.noark5.v5.Series] entity requires both ends to be synchronized. Only
the [addFile(nikita.common.model.noark5.v5.File file)] could be found. Consider adding
the [removeFile(nikita.common.model.noark5.v5.File file)]  synchronization method as well. For more info about this
event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.561 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
OneToOneWithoutMapsIdEvent - The [referencePrecursor] one-to-one association in
the [nikita.common.model.noark5.v5.Series] entity is using a separate Foreign Key to reference the parent record.
Consider using @MapsId so that the identifier is shared with the parent row. For more info about this event, check out
this User Guide link - https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#OneToOneWithoutMapsIdEvent
2021-03-14 18:24:43.563 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceRecord] bidirectional association in
the [nikita.common.model.noark5.v5.Series] entity requires both ends to be synchronized. Only
the [addRecord(nikita.common.model.noark5.v5.Record record)] could be found. Consider adding
the [removeRecord(nikita.common.model.noark5.v5.Record record)]  synchronization method as well. For more info about
this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.564 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceScreening] attribute in the [nikita.common.model.noark5.v5.Series] entity uses eager
fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more flexible when it
comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.565 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceStorageLocation] many-to-many association in
the [nikita.common.model.noark5.v5.Series] entity is using a List, so it does not render very efficient SQL statements.
Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:43.566 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyCascadeRemoveEvent - The [referenceStorageLocation] many-to-many association in
the [nikita.common.model.noark5.v5.Series] entity cascades the REMOVE operation. Cascading the REMOVE operation is not
needed since the other side is also a parent association. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyCascadeRemoveEvent
2021-03-14 18:24:43.572 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
OneToOneParentSideEvent - The [referenceSuccessor] one-to-one association in
the [class nikita.common.model.noark5.v5.Series] entity is mapped as the parent-side of this relationship. The
parent-side of a one-to-one association is fetched eagerly unless bytecode enhancement lazy loading is enabled and the
association is annotated with @LazyToOne(LazyToOneOption.NO_PROXY). For more info about this event, check out this User
Guide link - https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#OneToOneParentSideEvent
2021-03-14 18:24:43.599 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
TablePerClassInheritanceEvent - The [aud_nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePart_AUD]
entity uses the InheritanceType.TABLE_PER_CLASS strategy which renders very inefficient SQL statements. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#TablePerClassInheritanceEvent
2021-03-14 18:24:43.640 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.admin.AdministrativeUnit] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.646 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceBSMBase] bidirectional association in
the [nikita.common.model.noark5.v5.admin.AdministrativeUnit] entity requires both ends to be synchronized. Only
the [addBSMBase(nikita.common.model.noark5.bsm.BSMBase bSMBase)] could be found. Consider adding
the [removeBSMBase(nikita.common.model.noark5.bsm.BSMBase bSMBase)]  synchronization method as well. For more info about
this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.647 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceCaseFile] bidirectional association in
the [nikita.common.model.noark5.v5.admin.AdministrativeUnit] entity requires both ends to be synchronized. Consider
adding the [addCaseFile(nikita.common.model.noark5.v5.casehandling.CaseFile caseFile)]
and [removeCaseFile(nikita.common.model.noark5.v5.casehandling.CaseFile caseFile)] synchronization methods. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.649 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChildAdministrativeUnit] bidirectional association in
the [nikita.common.model.noark5.v5.admin.AdministrativeUnit] entity requires both ends to be synchronized. Consider
adding the [addAdministrativeUnit(nikita.common.model.noark5.v5.admin.AdministrativeUnit administrativeUnit)]
and [removeAdministrativeUnit(nikita.common.model.noark5.v5.admin.AdministrativeUnit administrativeUnit)]
synchronization methods. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.651 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceSequenceNumberGenerator] bidirectional association in
the [nikita.common.model.noark5.v5.admin.AdministrativeUnit] entity requires both ends to be synchronized. Only
the [addSequenceNumberGenerator(nikita.common.model.noark5.v5.casehandling.SequenceNumberGenerator sequenceNumberGenerator)]
could be found. Consider adding
the [removeSequenceNumberGenerator(nikita.common.model.noark5.v5.casehandling.SequenceNumberGenerator sequenceNumberGenerator)]
synchronization method as well. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.691 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.nationalidentifier.Plan] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.693 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceFile] attribute in the [nikita.common.model.noark5.v5.nationalidentifier.Plan] entity
uses eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more flexible
when it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.694 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceRecord] attribute in the [nikita.common.model.noark5.v5.nationalidentifier.Plan]
entity uses eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more
flexible when it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.722 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartInternal] entity requires both ends to be
synchronized. Only the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.725 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceBSMBase] bidirectional association in
the [nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartInternal] entity requires both ends to be
synchronized. Only the [addBSMBase(nikita.common.model.noark5.bsm.BSMBase bSMBase)] could be found. Consider adding
the [removeBSMBase(nikita.common.model.noark5.bsm.BSMBase bSMBase)]  synchronization method as well. For more info about
this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.726 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceRecord] attribute in
the [nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartInternal] entity uses eager fetching.
Consider using a lazy fetching which, not only that is more efficient, but it is way more flexible when it comes to
fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.729 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceAdministrativeUnit] attribute in
the [nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartInternal] entity uses eager fetching.
Consider using a lazy fetching which, not only that is more efficient, but it is way more flexible when it comes to
fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.730 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [user] attribute in
the [nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartInternal] entity uses eager fetching.
Consider using a lazy fetching which, not only that is more efficient, but it is way more flexible when it comes to
fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.740 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.Deletion] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.745 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceDocumentDescription] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.Deletion] entity requires both ends to be synchronized. Consider adding
the [addDocumentDescription(nikita.common.model.noark5.v5.DocumentDescription documentDescription)]
and [removeDocumentDescription(nikita.common.model.noark5.v5.DocumentDescription documentDescription)] synchronization
methods. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.746 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceSeries] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.Deletion] entity requires both ends to be synchronized. Consider adding
the [addSeries(nikita.common.model.noark5.v5.Series series)]
and [removeSeries(nikita.common.model.noark5.v5.Series series)] synchronization methods. For more info about this event,
check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.755 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.Comment] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.764 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceDocumentDescription] attribute in
the [nikita.common.model.noark5.v5.secondary.Comment] entity uses eager fetching. Consider using a lazy fetching which,
not only that is more efficient, but it is way more flexible when it comes to fetching data. For more info about this
event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.765 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceFile] attribute in the [nikita.common.model.noark5.v5.secondary.Comment] entity uses
eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more flexible when
it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.766 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceRecord] attribute in the [nikita.common.model.noark5.v5.secondary.Comment] entity
uses eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more flexible
when it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.788 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.nationalidentifier.DNumber] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.790 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceFile] attribute in the [nikita.common.model.noark5.v5.nationalidentifier.DNumber]
entity uses eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more
flexible when it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.792 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceRecord] attribute in the [nikita.common.model.noark5.v5.nationalidentifier.DNumber]
entity uses eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more
flexible when it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.823 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
TablePerClassInheritanceEvent - The [nikita.common.model.noark5.v5.nationalidentifier.NationalIdentifier] entity uses
the InheritanceType.TABLE_PER_CLASS strategy which renders very inefficient SQL statements. For more info about this
event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#TablePerClassInheritanceEvent
2021-03-14 18:24:43.831 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.nationalidentifier.NationalIdentifier] entity requires both ends to be synchronized.
Only the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.832 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceFile] attribute in
the [nikita.common.model.noark5.v5.nationalidentifier.NationalIdentifier] entity uses eager fetching. Consider using a
lazy fetching which, not only that is more efficient, but it is way more flexible when it comes to fetching data. For
more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.833 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceRecord] attribute in
the [nikita.common.model.noark5.v5.nationalidentifier.NationalIdentifier] entity uses eager fetching. Consider using a
lazy fetching which, not only that is more efficient, but it is way more flexible when it comes to fetching data. For
more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.851 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.Disposal] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.856 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceClass] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.Disposal] entity requires both ends to be synchronized. Consider adding
the [addClass(nikita.common.model.noark5.v5.Class class)] and [removeClass(nikita.common.model.noark5.v5.Class class)]
synchronization methods. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.857 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceDocumentDescription] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.Disposal] entity requires both ends to be synchronized. Consider adding
the [addDocumentDescription(nikita.common.model.noark5.v5.DocumentDescription documentDescription)]
and [removeDocumentDescription(nikita.common.model.noark5.v5.DocumentDescription documentDescription)] synchronization
methods. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.858 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceFile] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.Disposal] entity requires both ends to be synchronized. Consider adding
the [addFile(nikita.common.model.noark5.v5.File file)] and [removeFile(nikita.common.model.noark5.v5.File file)]
synchronization methods. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.860 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceRecord] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.Disposal] entity requires both ends to be synchronized. Consider adding
the [addRecord(nikita.common.model.noark5.v5.Record record)]
and [removeRecord(nikita.common.model.noark5.v5.Record record)] synchronization methods. For more info about this event,
check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.861 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceSeries] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.Disposal] entity requires both ends to be synchronized. Consider adding
the [addSeries(nikita.common.model.noark5.v5.Series series)]
and [removeSeries(nikita.common.model.noark5.v5.Series series)] synchronization methods. For more info about this event,
check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.870 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.CrossReference] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.907 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.ClassificationSystem] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.926 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceClass] bidirectional association in
the [nikita.common.model.noark5.v5.ClassificationSystem] entity requires both ends to be synchronized. Consider adding
the [addClass(nikita.common.model.noark5.v5.Class class)] and [removeClass(nikita.common.model.noark5.v5.Class class)]
synchronization methods. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.928 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceSeries] many-to-many association in
the [nikita.common.model.noark5.v5.ClassificationSystem] entity is using a List, so it does not render very efficient
SQL statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:43.928 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceSeries] bidirectional association in
the [nikita.common.model.noark5.v5.ClassificationSystem] entity requires both ends to be synchronized. Consider adding
the [addSeries(nikita.common.model.noark5.v5.Series series)]
and [removeSeries(nikita.common.model.noark5.v5.Series series)] synchronization methods. For more info about this event,
check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.936 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.Precedence] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.948 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceCaseFile] many-to-many association in
the [nikita.common.model.noark5.v5.secondary.Precedence] entity is using a List, so it does not render very efficient
SQL statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:43.948 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceCaseFile] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.Precedence] entity requires both ends to be synchronized. Consider adding
the [addCaseFile(nikita.common.model.noark5.v5.casehandling.CaseFile caseFile)]
and [removeCaseFile(nikita.common.model.noark5.v5.casehandling.CaseFile caseFile)] synchronization methods. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.951 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceRegistryEntry] many-to-many association in
the [nikita.common.model.noark5.v5.secondary.Precedence] entity is using a List, so it does not render very efficient
SQL statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:43.951 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceRegistryEntry] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.Precedence] entity requires both ends to be synchronized. Consider adding
the [addRegistryEntry(nikita.common.model.noark5.v5.casehandling.RegistryEntry registryEntry)]
and [removeRegistryEntry(nikita.common.model.noark5.v5.casehandling.RegistryEntry registryEntry)] synchronization
methods. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.961 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.FondsCreator] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.965 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceFonds] many-to-many association in the [nikita.common.model.noark5.v5.FondsCreator]
entity is using a List, so it does not render very efficient SQL statements. Consider using a Set instead. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:43.966 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceFonds] bidirectional association in
the [nikita.common.model.noark5.v5.FondsCreator] entity requires both ends to be synchronized. Only
the [addFonds(nikita.common.model.noark5.v5.Fonds fonds)] could be found. Consider adding
the [removeFonds(nikita.common.model.noark5.v5.Fonds fonds)]  synchronization method as well. For more info about this
event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.974 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.Author] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.978 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceDocumentDescription] attribute in
the [nikita.common.model.noark5.v5.secondary.Author] entity uses eager fetching. Consider using a lazy fetching which,
not only that is more efficient, but it is way more flexible when it comes to fetching data. For more info about this
event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.980 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceRecord] attribute in the [nikita.common.model.noark5.v5.secondary.Author] entity uses
eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more flexible when
it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:43.988 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.DisposalUndertaken] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.991 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceDocumentDescription] many-to-many association in
the [nikita.common.model.noark5.v5.secondary.DisposalUndertaken] entity is using a List, so it does not render very
efficient SQL statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:43.992 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceDocumentDescription] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.DisposalUndertaken] entity requires both ends to be synchronized. Consider
adding the [addDocumentDescription(nikita.common.model.noark5.v5.DocumentDescription documentDescription)]
and [removeDocumentDescription(nikita.common.model.noark5.v5.DocumentDescription documentDescription)] synchronization
methods. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:43.993 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceSeries] many-to-many association in
the [nikita.common.model.noark5.v5.secondary.DisposalUndertaken] entity is using a List, so it does not render very
efficient SQL statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:43.993 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceSeries] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.DisposalUndertaken] entity requires both ends to be synchronized. Consider
adding the [addSeries(nikita.common.model.noark5.v5.Series series)]
and [removeSeries(nikita.common.model.noark5.v5.Series series)] synchronization methods. For more info about this event,
check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.025 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.casehandling.secondary.BusinessAddress] entity requires both ends to be synchronized.
Only the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.026 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [correspondencePartUnit] attribute in
the [nikita.common.model.noark5.v5.casehandling.secondary.BusinessAddress] entity uses eager fetching. Consider using a
lazy fetching which, not only that is more efficient, but it is way more flexible when it comes to fetching data. For
more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:44.026 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
OneToOneWithoutMapsIdEvent - The [correspondencePartUnit] one-to-one association in
the [nikita.common.model.noark5.v5.casehandling.secondary.BusinessAddress] entity is using a separate Foreign Key to
reference the parent record. Consider using @MapsId so that the identifier is shared with the parent row. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#OneToOneWithoutMapsIdEvent
2021-03-14 18:24:44.027 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [partUnit] attribute in
the [nikita.common.model.noark5.v5.casehandling.secondary.BusinessAddress] entity uses eager fetching. Consider using a
lazy fetching which, not only that is more efficient, but it is way more flexible when it comes to fetching data. For
more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:44.027 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
OneToOneWithoutMapsIdEvent - The [partUnit] one-to-one association in
the [nikita.common.model.noark5.v5.casehandling.secondary.BusinessAddress] entity is using a separate Foreign Key to
reference the parent record. Consider using @MapsId so that the identifier is shared with the parent row. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#OneToOneWithoutMapsIdEvent
2021-03-14 18:24:44.055 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
TablePerClassInheritanceEvent - The [aud_nikita.common.model.noark5.v5.secondary.Part_AUD] entity uses the
InheritanceType.TABLE_PER_CLASS strategy which renders very inefficient SQL statements. For more info about this event,
check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#TablePerClassInheritanceEvent
2021-03-14 18:24:44.102 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.casehandling.secondary.PostalAddress] entity requires both ends to be synchronized.
Only the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.103 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
OneToOneWithoutMapsIdEvent - The [correspondencePartPerson] one-to-one association in
the [nikita.common.model.noark5.v5.casehandling.secondary.PostalAddress] entity is using a separate Foreign Key to
reference the parent record. Consider using @MapsId so that the identifier is shared with the parent row. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#OneToOneWithoutMapsIdEvent
2021-03-14 18:24:44.104 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
OneToOneWithoutMapsIdEvent - The [correspondencePartUnit] one-to-one association in
the [nikita.common.model.noark5.v5.casehandling.secondary.PostalAddress] entity is using a separate Foreign Key to
reference the parent record. Consider using @MapsId so that the identifier is shared with the parent row. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#OneToOneWithoutMapsIdEvent
2021-03-14 18:24:44.105 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
OneToOneWithoutMapsIdEvent - The [partPerson] one-to-one association in
the [nikita.common.model.noark5.v5.casehandling.secondary.PostalAddress] entity is using a separate Foreign Key to
reference the parent record. Consider using @MapsId so that the identifier is shared with the parent row. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#OneToOneWithoutMapsIdEvent
2021-03-14 18:24:44.106 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
OneToOneWithoutMapsIdEvent - The [partUnit] one-to-one association in
the [nikita.common.model.noark5.v5.casehandling.secondary.PostalAddress] entity is using a separate Foreign Key to
reference the parent record. Consider using @MapsId so that the identifier is shared with the parent row. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#OneToOneWithoutMapsIdEvent
2021-03-14 18:24:44.122 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartPerson] entity requires both ends to be
synchronized. Only the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.126 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceBSMBase] bidirectional association in
the [nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartPerson] entity requires both ends to be
synchronized. Only the [addBSMBase(nikita.common.model.noark5.bsm.BSMBase bSMBase)] could be found. Consider adding
the [removeBSMBase(nikita.common.model.noark5.bsm.BSMBase bSMBase)]  synchronization method as well. For more info about
this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.127 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceRecord] attribute in
the [nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartPerson] entity uses eager fetching. Consider
using a lazy fetching which, not only that is more efficient, but it is way more flexible when it comes to fetching
data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:44.131 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [contactInformation] attribute in
the [nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartPerson] entity uses eager fetching. Consider
using a lazy fetching which, not only that is more efficient, but it is way more flexible when it comes to fetching
data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:44.131 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
OneToOneParentSideEvent - The [contactInformation] one-to-one association in
the [class nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartPerson] entity is mapped as the
parent-side of this relationship. The parent-side of a one-to-one association is fetched eagerly unless bytecode
enhancement lazy loading is enabled and the association is annotated with @LazyToOne(LazyToOneOption.NO_PROXY). For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#OneToOneParentSideEvent
2021-03-14 18:24:44.132 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [postalAddress] attribute in
the [nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartPerson] entity uses eager fetching. Consider
using a lazy fetching which, not only that is more efficient, but it is way more flexible when it comes to fetching
data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:44.132 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
OneToOneParentSideEvent - The [postalAddress] one-to-one association in
the [class nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartPerson] entity is mapped as the
parent-side of this relationship. The parent-side of a one-to-one association is fetched eagerly unless bytecode
enhancement lazy loading is enabled and the association is annotated with @LazyToOne(LazyToOneOption.NO_PROXY). For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#OneToOneParentSideEvent
2021-03-14 18:24:44.133 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [residingAddress] attribute in
the [nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartPerson] entity uses eager fetching. Consider
using a lazy fetching which, not only that is more efficient, but it is way more flexible when it comes to fetching
data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:44.133 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
OneToOneParentSideEvent - The [residingAddress] one-to-one association in
the [class nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartPerson] entity is mapped as the
parent-side of this relationship. The parent-side of a one-to-one association is fetched eagerly unless bytecode
enhancement lazy loading is enabled and the association is annotated with @LazyToOne(LazyToOneOption.NO_PROXY). For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#OneToOneParentSideEvent
2021-03-14 18:24:44.169 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
TablePerClassInheritanceEvent - The [nikita.common.model.noark5.v5.SystemIdEntity] entity uses the
InheritanceType.TABLE_PER_CLASS strategy which renders very inefficient SQL statements. For more info about this event,
check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#TablePerClassInheritanceEvent
2021-03-14 18:24:44.177 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.SystemIdEntity] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.186 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.meeting.MeetingFile] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.195 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceBSMBase] bidirectional association in
the [nikita.common.model.noark5.v5.meeting.MeetingFile] entity requires both ends to be synchronized. Only
the [addBSMBase(nikita.common.model.noark5.bsm.BSMBase bSMBase)] could be found. Consider adding
the [removeBSMBase(nikita.common.model.noark5.bsm.BSMBase bSMBase)]  synchronization method as well. For more info about
this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.196 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChildFile] bidirectional association in
the [nikita.common.model.noark5.v5.meeting.MeetingFile] entity requires both ends to be synchronized. Consider adding
the [addFile(nikita.common.model.noark5.v5.File file)] and [removeFile(nikita.common.model.noark5.v5.File file)]
synchronization methods. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.198 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceClassified] attribute in the [nikita.common.model.noark5.v5.meeting.MeetingFile]
entity uses eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more
flexible when it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:44.199 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceComment] many-to-many association in
the [nikita.common.model.noark5.v5.meeting.MeetingFile] entity is using a List, so it does not render very efficient SQL
statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:44.201 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceCrossReference] bidirectional association in
the [nikita.common.model.noark5.v5.meeting.MeetingFile] entity requires both ends to be synchronized. Only
the [addCrossReference(nikita.common.model.noark5.v5.secondary.CrossReference crossReference)] could be found. Consider
adding the [removeCrossReference(nikita.common.model.noark5.v5.secondary.CrossReference crossReference)]
synchronization method as well. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.202 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceDisposal] attribute in the [nikita.common.model.noark5.v5.meeting.MeetingFile] entity
uses eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more flexible
when it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:44.203 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceKeyword] many-to-many association in
the [nikita.common.model.noark5.v5.meeting.MeetingFile] entity is using a List, so it does not render very efficient SQL
statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:44.204 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceNationalIdentifier] bidirectional association in
the [nikita.common.model.noark5.v5.meeting.MeetingFile] entity requires both ends to be synchronized. Only
the [addNationalIdentifier(nikita.common.model.noark5.v5.nationalidentifier.NationalIdentifier nationalIdentifier)]
could be found. Consider adding
the [removeNationalIdentifier(nikita.common.model.noark5.v5.nationalidentifier.NationalIdentifier nationalIdentifier)]
synchronization method as well. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.206 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referencePart] many-to-many association in
the [nikita.common.model.noark5.v5.meeting.MeetingFile] entity is using a List, so it does not render very efficient SQL
statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:44.207 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceRecord] bidirectional association in
the [nikita.common.model.noark5.v5.meeting.MeetingFile] entity requires both ends to be synchronized. Consider adding
the [addRecord(nikita.common.model.noark5.v5.Record record)]
and [removeRecord(nikita.common.model.noark5.v5.Record record)] synchronization methods. For more info about this event,
check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.209 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceScreening] attribute in the [nikita.common.model.noark5.v5.meeting.MeetingFile]
entity uses eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more
flexible when it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:44.211 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceStorageLocation] many-to-many association in
the [nikita.common.model.noark5.v5.meeting.MeetingFile] entity is using a List, so it does not render very efficient SQL
statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:44.216 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceMeetingParticipant] bidirectional association in
the [nikita.common.model.noark5.v5.meeting.MeetingFile] entity requires both ends to be synchronized. Consider adding
the [addMeetingParticipant(nikita.common.model.noark5.v5.meeting.MeetingParticipant meetingParticipant)]
and [removeMeetingParticipant(nikita.common.model.noark5.v5.meeting.MeetingParticipant meetingParticipant)]
synchronization methods. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.217 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
OneToOneWithoutMapsIdEvent - The [referenceNextMeeting] one-to-one association in
the [nikita.common.model.noark5.v5.meeting.MeetingFile] entity is using a separate Foreign Key to reference the parent
record. Consider using @MapsId so that the identifier is shared with the parent row. For more info about this event,
check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#OneToOneWithoutMapsIdEvent
2021-03-14 18:24:44.218 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
OneToOneWithoutMapsIdEvent - The [referencePreviousMeeting] one-to-one association in
the [nikita.common.model.noark5.v5.meeting.MeetingFile] entity is using a separate Foreign Key to reference the parent
record. Consider using @MapsId so that the identifier is shared with the parent row. For more info about this event,
check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#OneToOneWithoutMapsIdEvent
2021-03-14 18:24:44.243 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.meeting.MeetingRecord] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.251 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceAuthor] bidirectional association in
the [nikita.common.model.noark5.v5.meeting.MeetingRecord] entity requires both ends to be synchronized. Only
the [addAuthor(nikita.common.model.noark5.v5.secondary.Author author)] could be found. Consider adding
the [removeAuthor(nikita.common.model.noark5.v5.secondary.Author author)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.252 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceBSMBase] bidirectional association in
the [nikita.common.model.noark5.v5.meeting.MeetingRecord] entity requires both ends to be synchronized. Only
the [addBSMBase(nikita.common.model.noark5.bsm.BSMBase bSMBase)] could be found. Consider adding
the [removeBSMBase(nikita.common.model.noark5.bsm.BSMBase bSMBase)]  synchronization method as well. For more info about
this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.254 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceClassified] attribute in the [nikita.common.model.noark5.v5.meeting.MeetingRecord]
entity uses eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more
flexible when it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:44.255 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceComment] many-to-many association in
the [nikita.common.model.noark5.v5.meeting.MeetingRecord] entity is using a List, so it does not render very efficient
SQL statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:44.257 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceCorrespondencePart] bidirectional association in
the [nikita.common.model.noark5.v5.meeting.MeetingRecord] entity requires both ends to be synchronized. Only
the [addCorrespondencePart(nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePart correspondencePart)]
could be found. Consider adding
the [removeCorrespondencePart(nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePart correspondencePart)]
synchronization method as well. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.258 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceCrossReference] bidirectional association in
the [nikita.common.model.noark5.v5.meeting.MeetingRecord] entity requires both ends to be synchronized. Only
the [addCrossReference(nikita.common.model.noark5.v5.secondary.CrossReference crossReference)] could be found. Consider
adding the [removeCrossReference(nikita.common.model.noark5.v5.secondary.CrossReference crossReference)]
synchronization method as well. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.259 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceDisposal] attribute in the [nikita.common.model.noark5.v5.meeting.MeetingRecord]
entity uses eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more
flexible when it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:44.260 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceDocumentDescription] many-to-many association in
the [nikita.common.model.noark5.v5.meeting.MeetingRecord] entity is using a List, so it does not render very efficient
SQL statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:44.262 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceKeyword] many-to-many association in
the [nikita.common.model.noark5.v5.meeting.MeetingRecord] entity is using a List, so it does not render very efficient
SQL statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:44.263 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceNationalIdentifier] bidirectional association in
the [nikita.common.model.noark5.v5.meeting.MeetingRecord] entity requires both ends to be synchronized. Only
the [addNationalIdentifier(nikita.common.model.noark5.v5.nationalidentifier.NationalIdentifier nationalIdentifier)]
could be found. Consider adding
the [removeNationalIdentifier(nikita.common.model.noark5.v5.nationalidentifier.NationalIdentifier nationalIdentifier)]
synchronization method as well. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.265 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referencePart] many-to-many association in
the [nikita.common.model.noark5.v5.meeting.MeetingRecord] entity is using a List, so it does not render very efficient
SQL statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:44.266 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceScreening] attribute in the [nikita.common.model.noark5.v5.meeting.MeetingRecord]
entity uses eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more
flexible when it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:44.268 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceStorageLocation] many-to-many association in
the [nikita.common.model.noark5.v5.meeting.MeetingRecord] entity is using a List, so it does not render very efficient
SQL statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:44.275 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
OneToOneWithoutMapsIdEvent - The [referenceFromMeetingRegistration] one-to-one association in
the [nikita.common.model.noark5.v5.meeting.MeetingRecord] entity is using a separate Foreign Key to reference the parent
record. Consider using @MapsId so that the identifier is shared with the parent row. For more info about this event,
check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#OneToOneWithoutMapsIdEvent
2021-03-14 18:24:44.276 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
OneToOneWithoutMapsIdEvent - The [referenceToMeetingRegistration] one-to-one association in
the [nikita.common.model.noark5.v5.meeting.MeetingRecord] entity is using a separate Foreign Key to reference the parent
record. Consider using @MapsId so that the identifier is shared with the parent row. For more info about this event,
check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#OneToOneWithoutMapsIdEvent
2021-03-14 18:24:44.298 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
TablePerClassInheritanceEvent - The [nikita.common.model.noark5.v5.File] entity uses the InheritanceType.TABLE_PER_CLASS
strategy which renders very inefficient SQL statements. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#TablePerClassInheritanceEvent
2021-03-14 18:24:44.307 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.File] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.316 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceBSMBase] bidirectional association in
the [nikita.common.model.noark5.v5.File] entity requires both ends to be synchronized. Only
the [addBSMBase(nikita.common.model.noark5.bsm.BSMBase bSMBase)] could be found. Consider adding
the [removeBSMBase(nikita.common.model.noark5.bsm.BSMBase bSMBase)]  synchronization method as well. For more info about
this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.317 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChildFile] bidirectional association in
the [nikita.common.model.noark5.v5.File] entity requires both ends to be synchronized. Consider adding
the [addFile(nikita.common.model.noark5.v5.File file)] and [removeFile(nikita.common.model.noark5.v5.File file)]
synchronization methods. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.319 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceClassified] attribute in the [nikita.common.model.noark5.v5.File] entity uses eager
fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more flexible when it
comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:44.320 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceComment] many-to-many association in the [nikita.common.model.noark5.v5.File] entity
is using a List, so it does not render very efficient SQL statements. Consider using a Set instead. For more info about
this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:44.321 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceCrossReference] bidirectional association in
the [nikita.common.model.noark5.v5.File] entity requires both ends to be synchronized. Only
the [addCrossReference(nikita.common.model.noark5.v5.secondary.CrossReference crossReference)] could be found. Consider
adding the [removeCrossReference(nikita.common.model.noark5.v5.secondary.CrossReference crossReference)]
synchronization method as well. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.322 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceDisposal] attribute in the [nikita.common.model.noark5.v5.File] entity uses eager
fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more flexible when it
comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:44.323 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceKeyword] many-to-many association in the [nikita.common.model.noark5.v5.File] entity
is using a List, so it does not render very efficient SQL statements. Consider using a Set instead. For more info about
this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:44.324 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceNationalIdentifier] bidirectional association in
the [nikita.common.model.noark5.v5.File] entity requires both ends to be synchronized. Only
the [addNationalIdentifier(nikita.common.model.noark5.v5.nationalidentifier.NationalIdentifier nationalIdentifier)]
could be found. Consider adding
the [removeNationalIdentifier(nikita.common.model.noark5.v5.nationalidentifier.NationalIdentifier nationalIdentifier)]
synchronization method as well. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.326 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referencePart] many-to-many association in the [nikita.common.model.noark5.v5.File] entity is
using a List, so it does not render very efficient SQL statements. Consider using a Set instead. For more info about
this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:44.328 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceRecord] bidirectional association in
the [nikita.common.model.noark5.v5.File] entity requires both ends to be synchronized. Consider adding
the [addRecord(nikita.common.model.noark5.v5.Record record)]
and [removeRecord(nikita.common.model.noark5.v5.Record record)] synchronization methods. For more info about this event,
check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.329 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceScreening] attribute in the [nikita.common.model.noark5.v5.File] entity uses eager
fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more flexible when it
comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:44.331 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceStorageLocation] many-to-many association in
the [nikita.common.model.noark5.v5.File] entity is using a List, so it does not render very efficient SQL statements.
Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:44.406 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.casehandling.secondary.ResidingAddress] entity requires both ends to be synchronized.
Only the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.407 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
OneToOneWithoutMapsIdEvent - The [correspondencePartPerson] one-to-one association in
the [nikita.common.model.noark5.v5.casehandling.secondary.ResidingAddress] entity is using a separate Foreign Key to
reference the parent record. Consider using @MapsId so that the identifier is shared with the parent row. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#OneToOneWithoutMapsIdEvent
2021-03-14 18:24:44.409 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
OneToOneWithoutMapsIdEvent - The [partPerson] one-to-one association in
the [nikita.common.model.noark5.v5.casehandling.secondary.ResidingAddress] entity is using a separate Foreign Key to
reference the parent record. Consider using @MapsId so that the identifier is shared with the parent row. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#OneToOneWithoutMapsIdEvent
2021-03-14 18:24:44.410 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
TablePerClassInheritanceEvent - The [aud_nikita.common.model.noark5.v5.nationalidentifier.NationalIdentifier_AUD] entity
uses the InheritanceType.TABLE_PER_CLASS strategy which renders very inefficient SQL statements. For more info about
this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#TablePerClassInheritanceEvent
2021-03-14 18:24:44.426 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.nationalidentifier.SocialSecurityNumber] entity requires both ends to be
synchronized. Only the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.427 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceFile] attribute in
the [nikita.common.model.noark5.v5.nationalidentifier.SocialSecurityNumber] entity uses eager fetching. Consider using a
lazy fetching which, not only that is more efficient, but it is way more flexible when it comes to fetching data. For
more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:44.428 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceRecord] attribute in
the [nikita.common.model.noark5.v5.nationalidentifier.SocialSecurityNumber] entity uses eager fetching. Consider using a
lazy fetching which, not only that is more efficient, but it is way more flexible when it comes to fetching data. For
more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:44.451 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
TablePerClassInheritanceEvent - The [nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePart] entity
uses the InheritanceType.TABLE_PER_CLASS strategy which renders very inefficient SQL statements. For more info about
this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#TablePerClassInheritanceEvent
2021-03-14 18:24:44.459 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePart] entity requires both ends to be
synchronized. Only the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.462 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceBSMBase] bidirectional association in
the [nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePart] entity requires both ends to be
synchronized. Only the [addBSMBase(nikita.common.model.noark5.bsm.BSMBase bSMBase)] could be found. Consider adding
the [removeBSMBase(nikita.common.model.noark5.bsm.BSMBase bSMBase)]  synchronization method as well. For more info about
this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.463 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceRecord] attribute in
the [nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePart] entity uses eager fetching. Consider using
a lazy fetching which, not only that is more efficient, but it is way more flexible when it comes to fetching data. For
more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:44.514 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.DocumentFlow] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.528 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
TablePerClassInheritanceEvent - The [nikita.common.model.noark5.v5.secondary.Part] entity uses the
InheritanceType.TABLE_PER_CLASS strategy which renders very inefficient SQL statements. For more info about this event,
check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#TablePerClassInheritanceEvent
2021-03-14 18:24:44.536 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.Part] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.543 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceBSMBase] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.Part] entity requires both ends to be synchronized. Only
the [addBSMBase(nikita.common.model.noark5.bsm.BSMBase bSMBase)] could be found. Consider adding
the [removeBSMBase(nikita.common.model.noark5.bsm.BSMBase bSMBase)]  synchronization method as well. For more info about
this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.544 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceDocumentDescription] many-to-many association in
the [nikita.common.model.noark5.v5.secondary.Part] entity is using a List, so it does not render very efficient SQL
statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:44.544 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceDocumentDescription] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.Part] entity requires both ends to be synchronized. Only
the [addDocumentDescription(nikita.common.model.noark5.v5.DocumentDescription documentDescription)] could be found.
Consider adding the [removeDocumentDescription(nikita.common.model.noark5.v5.DocumentDescription documentDescription)]
synchronization method as well. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.545 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceFile] many-to-many association in the [nikita.common.model.noark5.v5.secondary.Part]
entity is using a List, so it does not render very efficient SQL statements. Consider using a Set instead. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:44.546 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceFile] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.Part] entity requires both ends to be synchronized. Only
the [addFile(nikita.common.model.noark5.v5.File file)] could be found. Consider adding
the [removeFile(nikita.common.model.noark5.v5.File file)]  synchronization method as well. For more info about this
event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.547 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceRecord] many-to-many association in
the [nikita.common.model.noark5.v5.secondary.Part] entity is using a List, so it does not render very efficient SQL
statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:44.547 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceRecord] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.Part] entity requires both ends to be synchronized. Only
the [addRecord(nikita.common.model.noark5.v5.Record record)] could be found. Consider adding
the [removeRecord(nikita.common.model.noark5.v5.Record record)]  synchronization method as well. For more info about
this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.555 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.Conversion] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.572 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.SignOff] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.574 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceRecord] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.SignOff] entity requires both ends to be synchronized. Only
the [addRegistryEntry(nikita.common.model.noark5.v5.casehandling.RegistryEntry registryEntry)] could be found. Consider
adding the [removeRegistryEntry(nikita.common.model.noark5.v5.casehandling.RegistryEntry registryEntry)]
synchronization method as well. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.575 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceSignedOffCorrespondencePart] attribute in
the [nikita.common.model.noark5.v5.secondary.SignOff] entity uses eager fetching. Consider using a lazy fetching which,
not only that is more efficient, but it is way more flexible when it comes to fetching data. For more info about this
event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:44.575 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
OneToOneWithoutMapsIdEvent - The [referenceSignedOffCorrespondencePart] one-to-one association in
the [nikita.common.model.noark5.v5.secondary.SignOff] entity is using a separate Foreign Key to reference the parent
record. Consider using @MapsId so that the identifier is shared with the parent row. For more info about this event,
check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#OneToOneWithoutMapsIdEvent
2021-03-14 18:24:44.577 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceSignedOffRecord] attribute in the [nikita.common.model.noark5.v5.secondary.SignOff]
entity uses eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more
flexible when it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:44.577 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
OneToOneWithoutMapsIdEvent - The [referenceSignedOffRecord] one-to-one association in
the [nikita.common.model.noark5.v5.secondary.SignOff] entity is using a separate Foreign Key to reference the parent
record. Consider using @MapsId so that the identifier is shared with the parent row. For more info about this event,
check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#OneToOneWithoutMapsIdEvent
2021-03-14 18:24:44.600 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.Class] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.606 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChildClass] bidirectional association in
the [nikita.common.model.noark5.v5.Class] entity requires both ends to be synchronized. Consider adding
the [addClass(nikita.common.model.noark5.v5.Class class)] and [removeClass(nikita.common.model.noark5.v5.Class class)]
synchronization methods. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.608 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceClassified] attribute in the [nikita.common.model.noark5.v5.Class] entity uses eager
fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more flexible when it
comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:44.610 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceCrossReference] bidirectional association in
the [nikita.common.model.noark5.v5.Class] entity requires both ends to be synchronized. Only
the [addCrossReference(nikita.common.model.noark5.v5.secondary.CrossReference crossReference)] could be found. Consider
adding the [removeCrossReference(nikita.common.model.noark5.v5.secondary.CrossReference crossReference)]
synchronization method as well. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.611 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceDisposal] attribute in the [nikita.common.model.noark5.v5.Class] entity uses eager
fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more flexible when it
comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:44.612 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceFile] bidirectional association in
the [nikita.common.model.noark5.v5.Class] entity requires both ends to be synchronized. Consider adding
the [addFile(nikita.common.model.noark5.v5.File file)] and [removeFile(nikita.common.model.noark5.v5.File file)]
synchronization methods. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.613 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceKeyword] many-to-many association in the [nikita.common.model.noark5.v5.Class]
entity is using a List, so it does not render very efficient SQL statements. Consider using a Set instead. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:44.615 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceRecord] bidirectional association in
the [nikita.common.model.noark5.v5.Class] entity requires both ends to be synchronized. Consider adding
the [addRecord(nikita.common.model.noark5.v5.Record record)]
and [removeRecord(nikita.common.model.noark5.v5.Record record)] synchronization methods. For more info about this event,
check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.617 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceScreening] attribute in the [nikita.common.model.noark5.v5.Class] entity uses eager
fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more flexible when it
comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:44.631 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.PartPerson] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.639 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceBSMBase] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.PartPerson] entity requires both ends to be synchronized. Only
the [addBSMBase(nikita.common.model.noark5.bsm.BSMBase bSMBase)] could be found. Consider adding
the [removeBSMBase(nikita.common.model.noark5.bsm.BSMBase bSMBase)]  synchronization method as well. For more info about
this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.640 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceDocumentDescription] many-to-many association in
the [nikita.common.model.noark5.v5.secondary.PartPerson] entity is using a List, so it does not render very efficient
SQL statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:44.640 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceDocumentDescription] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.PartPerson] entity requires both ends to be synchronized. Only
the [addDocumentDescription(nikita.common.model.noark5.v5.DocumentDescription documentDescription)] could be found.
Consider adding the [removeDocumentDescription(nikita.common.model.noark5.v5.DocumentDescription documentDescription)]
synchronization method as well. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.641 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceFile] many-to-many association in
the [nikita.common.model.noark5.v5.secondary.PartPerson] entity is using a List, so it does not render very efficient
SQL statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:44.641 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceFile] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.PartPerson] entity requires both ends to be synchronized. Only
the [addFile(nikita.common.model.noark5.v5.File file)] could be found. Consider adding
the [removeFile(nikita.common.model.noark5.v5.File file)]  synchronization method as well. For more info about this
event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.642 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceRecord] many-to-many association in
the [nikita.common.model.noark5.v5.secondary.PartPerson] entity is using a List, so it does not render very efficient
SQL statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:44.642 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceRecord] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.PartPerson] entity requires both ends to be synchronized. Only
the [addRecord(nikita.common.model.noark5.v5.Record record)] could be found. Consider adding
the [removeRecord(nikita.common.model.noark5.v5.Record record)]  synchronization method as well. For more info about
this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.646 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [contactInformation] attribute in the [nikita.common.model.noark5.v5.secondary.PartPerson]
entity uses eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more
flexible when it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:44.647 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
OneToOneParentSideEvent - The [contactInformation] one-to-one association in
the [class nikita.common.model.noark5.v5.secondary.PartPerson] entity is mapped as the parent-side of this relationship.
The parent-side of a one-to-one association is fetched eagerly unless bytecode enhancement lazy loading is enabled and
the association is annotated with @LazyToOne(LazyToOneOption.NO_PROXY). For more info about this event, check out this
User Guide link - https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#OneToOneParentSideEvent
2021-03-14 18:24:44.648 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [postalAddress] attribute in the [nikita.common.model.noark5.v5.secondary.PartPerson] entity
uses eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more flexible
when it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:44.648 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
OneToOneParentSideEvent - The [postalAddress] one-to-one association in
the [class nikita.common.model.noark5.v5.secondary.PartPerson] entity is mapped as the parent-side of this relationship.
The parent-side of a one-to-one association is fetched eagerly unless bytecode enhancement lazy loading is enabled and
the association is annotated with @LazyToOne(LazyToOneOption.NO_PROXY). For more info about this event, check out this
User Guide link - https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#OneToOneParentSideEvent
2021-03-14 18:24:44.649 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [residingAddress] attribute in the [nikita.common.model.noark5.v5.secondary.PartPerson] entity
uses eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more flexible
when it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:44.649 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
OneToOneParentSideEvent - The [residingAddress] one-to-one association in
the [class nikita.common.model.noark5.v5.secondary.PartPerson] entity is mapped as the parent-side of this relationship.
The parent-side of a one-to-one association is fetched eagerly unless bytecode enhancement lazy loading is enabled and
the association is annotated with @LazyToOne(LazyToOneOption.NO_PROXY). For more info about this event, check out this
User Guide link - https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#OneToOneParentSideEvent
2021-03-14 18:24:44.679 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.nationalidentifier.Position] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.681 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceFile] attribute in the [nikita.common.model.noark5.v5.nationalidentifier.Position]
entity uses eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more
flexible when it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:44.682 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceRecord] attribute in the [nikita.common.model.noark5.v5.nationalidentifier.Position]
entity uses eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more
flexible when it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:44.705 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.admin.User] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.708 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [administrativeUnits] many-to-many association in
the [nikita.common.model.noark5.v5.admin.User] entity is using a List, so it does not render very efficient SQL
statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:44.708 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [administrativeUnits] bidirectional association in
the [nikita.common.model.noark5.v5.admin.User] entity requires both ends to be synchronized. Only
the [addAdministrativeUnit(nikita.common.model.noark5.v5.admin.AdministrativeUnit administrativeUnit)] could be found.
Consider adding
the [removeAdministrativeUnit(nikita.common.model.noark5.v5.admin.AdministrativeUnit administrativeUnit)]
synchronization method as well. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.709 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [authorities] attribute in the [nikita.common.model.noark5.v5.admin.User] entity uses eager
fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more flexible when it
comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:44.709 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [authorities] many-to-many association in the [nikita.common.model.noark5.v5.admin.User]
entity is using a List, so it does not render very efficient SQL statements. Consider using a Set instead. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:44.726 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.Screening] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.730 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceClass] many-to-many association in
the [nikita.common.model.noark5.v5.secondary.Screening] entity is using a List, so it does not render very efficient SQL
statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:44.730 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceClass] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.Screening] entity requires both ends to be synchronized. Consider adding
the [addClass(nikita.common.model.noark5.v5.Class class)] and [removeClass(nikita.common.model.noark5.v5.Class class)]
synchronization methods. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.731 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceDocumentDescription] many-to-many association in
the [nikita.common.model.noark5.v5.secondary.Screening] entity is using a List, so it does not render very efficient SQL
statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:44.731 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceDocumentDescription] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.Screening] entity requires both ends to be synchronized. Consider adding
the [addDocumentDescription(nikita.common.model.noark5.v5.DocumentDescription documentDescription)]
and [removeDocumentDescription(nikita.common.model.noark5.v5.DocumentDescription documentDescription)] synchronization
methods. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.732 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceFile] many-to-many association in
the [nikita.common.model.noark5.v5.secondary.Screening] entity is using a List, so it does not render very efficient SQL
statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:44.732 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceFile] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.Screening] entity requires both ends to be synchronized. Consider adding
the [addFile(nikita.common.model.noark5.v5.File file)] and [removeFile(nikita.common.model.noark5.v5.File file)]
synchronization methods. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.733 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceRecord] many-to-many association in
the [nikita.common.model.noark5.v5.secondary.Screening] entity is using a List, so it does not render very efficient SQL
statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:44.733 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceRecord] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.Screening] entity requires both ends to be synchronized. Consider adding
the [addRecord(nikita.common.model.noark5.v5.Record record)]
and [removeRecord(nikita.common.model.noark5.v5.Record record)] synchronization methods. For more info about this event,
check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.734 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceSeries] many-to-many association in
the [nikita.common.model.noark5.v5.secondary.Screening] entity is using a List, so it does not render very efficient SQL
statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:44.735 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceSeries] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.Screening] entity requires both ends to be synchronized. Consider adding
the [addSeries(nikita.common.model.noark5.v5.Series series)]
and [removeSeries(nikita.common.model.noark5.v5.Series series)] synchronization methods. For more info about this event,
check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.773 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
TablePerClassInheritanceEvent - The [aud_nikita.common.model.noark5.v5.Record_AUD] entity uses the
InheritanceType.TABLE_PER_CLASS strategy which renders very inefficient SQL statements. For more info about this event,
check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#TablePerClassInheritanceEvent
2021-03-14 18:24:44.845 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.nationalidentifier.Building] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.847 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceFile] attribute in the [nikita.common.model.noark5.v5.nationalidentifier.Building]
entity uses eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more
flexible when it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:44.848 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceRecord] attribute in the [nikita.common.model.noark5.v5.nationalidentifier.Building]
entity uses eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more
flexible when it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:44.868 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.casehandling.secondary.ContactInformation] entity requires both ends to be
synchronized. Only the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.869 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [correspondencePartPerson] attribute in
the [nikita.common.model.noark5.v5.casehandling.secondary.ContactInformation] entity uses eager fetching. Consider using
a lazy fetching which, not only that is more efficient, but it is way more flexible when it comes to fetching data. For
more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:44.869 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
OneToOneWithoutMapsIdEvent - The [correspondencePartPerson] one-to-one association in
the [nikita.common.model.noark5.v5.casehandling.secondary.ContactInformation] entity is using a separate Foreign Key to
reference the parent record. Consider using @MapsId so that the identifier is shared with the parent row. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#OneToOneWithoutMapsIdEvent
2021-03-14 18:24:44.870 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [correspondencePartUnit] attribute in
the [nikita.common.model.noark5.v5.casehandling.secondary.ContactInformation] entity uses eager fetching. Consider using
a lazy fetching which, not only that is more efficient, but it is way more flexible when it comes to fetching data. For
more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:44.870 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
OneToOneWithoutMapsIdEvent - The [correspondencePartUnit] one-to-one association in
the [nikita.common.model.noark5.v5.casehandling.secondary.ContactInformation] entity is using a separate Foreign Key to
reference the parent record. Consider using @MapsId so that the identifier is shared with the parent row. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#OneToOneWithoutMapsIdEvent
2021-03-14 18:24:44.873 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [partPerson] attribute in
the [nikita.common.model.noark5.v5.casehandling.secondary.ContactInformation] entity uses eager fetching. Consider using
a lazy fetching which, not only that is more efficient, but it is way more flexible when it comes to fetching data. For
more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:44.873 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
OneToOneWithoutMapsIdEvent - The [partPerson] one-to-one association in
the [nikita.common.model.noark5.v5.casehandling.secondary.ContactInformation] entity is using a separate Foreign Key to
reference the parent record. Consider using @MapsId so that the identifier is shared with the parent row. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#OneToOneWithoutMapsIdEvent
2021-03-14 18:24:44.874 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [partUnit] attribute in
the [nikita.common.model.noark5.v5.casehandling.secondary.ContactInformation] entity uses eager fetching. Consider using
a lazy fetching which, not only that is more efficient, but it is way more flexible when it comes to fetching data. For
more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:44.875 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
OneToOneWithoutMapsIdEvent - The [partUnit] one-to-one association in
the [nikita.common.model.noark5.v5.casehandling.secondary.ContactInformation] entity is using a separate Foreign Key to
reference the parent record. Consider using @MapsId so that the identifier is shared with the parent row. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#OneToOneWithoutMapsIdEvent
2021-03-14 18:24:44.943 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.Fonds] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.952 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChildFonds] bidirectional association in
the [nikita.common.model.noark5.v5.Fonds] entity requires both ends to be synchronized. Only
the [addFonds(nikita.common.model.noark5.v5.Fonds fonds)] could be found. Consider adding
the [removeFonds(nikita.common.model.noark5.v5.Fonds fonds)]  synchronization method as well. For more info about this
event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.954 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceFondsCreator] many-to-many association in the [nikita.common.model.noark5.v5.Fonds]
entity is using a List, so it does not render very efficient SQL statements. Consider using a Set instead. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:44.956 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceSeries] bidirectional association in
the [nikita.common.model.noark5.v5.Fonds] entity requires both ends to be synchronized. Consider adding
the [addSeries(nikita.common.model.noark5.v5.Series series)]
and [removeSeries(nikita.common.model.noark5.v5.Series series)] synchronization methods. For more info about this event,
check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.957 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceStorageLocation] many-to-many association in
the [nikita.common.model.noark5.v5.Fonds] entity is using a List, so it does not render very efficient SQL statements.
Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:44.975 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.md_other.BSMMetadata] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:44.999 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.nationalidentifier.Unit] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:45.000 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceFile] attribute in the [nikita.common.model.noark5.v5.nationalidentifier.Unit] entity
uses eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more flexible
when it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:45.002 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceRecord] attribute in the [nikita.common.model.noark5.v5.nationalidentifier.Unit]
entity uses eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more
flexible when it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:45.012 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.nationalidentifier.CadastralUnit] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:45.013 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceFile] attribute in
the [nikita.common.model.noark5.v5.nationalidentifier.CadastralUnit] entity uses eager fetching. Consider using a lazy
fetching which, not only that is more efficient, but it is way more flexible when it comes to fetching data. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:45.014 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceRecord] attribute in
the [nikita.common.model.noark5.v5.nationalidentifier.CadastralUnit] entity uses eager fetching. Consider using a lazy
fetching which, not only that is more efficient, but it is way more flexible when it comes to fetching data. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:45.033 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.StorageLocation] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:45.035 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceFile] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.StorageLocation] entity requires both ends to be synchronized. Consider
adding the [addFile(nikita.common.model.noark5.v5.File file)] and [removeFile(nikita.common.model.noark5.v5.File file)]
synchronization methods. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:45.036 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceFonds] many-to-many association in
the [nikita.common.model.noark5.v5.secondary.StorageLocation] entity is using a List, so it does not render very
efficient SQL statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:45.036 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceFonds] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.StorageLocation] entity requires both ends to be synchronized. Consider
adding the [addFonds(nikita.common.model.noark5.v5.Fonds fonds)]
and [removeFonds(nikita.common.model.noark5.v5.Fonds fonds)] synchronization methods. For more info about this event,
check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:45.037 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceRecord] many-to-many association in
the [nikita.common.model.noark5.v5.secondary.StorageLocation] entity is using a List, so it does not render very
efficient SQL statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:45.037 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceRecord] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.StorageLocation] entity requires both ends to be synchronized. Consider
adding the [addRecord(nikita.common.model.noark5.v5.Record record)]
and [removeRecord(nikita.common.model.noark5.v5.Record record)] synchronization methods. For more info about this event,
check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:45.038 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
ManyToManyListEvent - The [referenceSeries] many-to-many association in
the [nikita.common.model.noark5.v5.secondary.StorageLocation] entity is using a List, so it does not render very
efficient SQL statements. Consider using a Set instead. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#ManyToManyListEvent
2021-03-14 18:24:45.038 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceSeries] bidirectional association in
the [nikita.common.model.noark5.v5.secondary.StorageLocation] entity requires both ends to be synchronized. Consider
adding the [addSeries(nikita.common.model.noark5.v5.Series series)]
and [removeSeries(nikita.common.model.noark5.v5.Series series)] synchronization methods. For more info about this event,
check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:45.088 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceChangeLog] bidirectional association in
the [nikita.common.model.noark5.v5.DocumentObject] entity requires both ends to be synchronized. Only
the [addChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)] could be found. Consider adding
the [removeChangeLog(nikita.common.model.noark5.v5.ChangeLog changeLog)]  synchronization method as well. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:45.097 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
BidirectionalSynchronizationEvent - The [referenceConversion] bidirectional association in
the [nikita.common.model.noark5.v5.DocumentObject] entity requires both ends to be synchronized. Only
the [addConversion(nikita.common.model.noark5.v5.secondary.Conversion conversion)] could be found. Consider adding
the [removeConversion(nikita.common.model.noark5.v5.secondary.Conversion conversion)]  synchronization method as well.
For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#BidirectionalSynchronizationEvent
2021-03-14 18:24:45.098 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceDocumentDescription] attribute in the [nikita.common.model.noark5.v5.DocumentObject]
entity uses eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more
flexible when it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:45.103 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
EagerFetchingEvent - The [referenceElectronicSignature] attribute in the [nikita.common.model.noark5.v5.DocumentObject]
entity uses eager fetching. Consider using a lazy fetching which, not only that is more efficient, but it is way more
flexible when it comes to fetching data. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EagerFetchingEvent
2021-03-14 18:24:45.116 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
JdbcBatchSizeEvent - If you set the [hibernate.jdbc.batch_size] configuration property to a value greater than 1 (
usually between 5 and 30), Hibernate can then execute SQL statements in batches, therefore reducing the number of
database network roundtrips. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#JdbcBatchSizeEvent
2021-03-14 18:24:45.126 ERROR 28858 --- [           main] Hypersistence Optimizer                  : CRITICAL -
QueryPaginationCollectionFetchingEvent - You should set the [hibernate.query.fail_on_pagination_over_collection_fetch]
configuration property to the value of [true], as Hibernate can then prevent in-memory pagination when join fetching a
child entity collection. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#QueryPaginationCollectionFetchingEvent
2021-03-14 18:24:45.129 WARN 28858 --- [           main] Hypersistence Optimizer                  : 491 issues were
found: 1 BLOCKER, 377 CRITICAL, 19 MAJOR, 94 MINOR 2021-03-14 18:32:31.319 ERROR 28858 --- [nio-8092-exec-9]
Hypersistence Optimizer                  : CRITICAL - QueryResultListSizeEvent -
The [select generatedAlias0 from Country as generatedAlias0] query returned a List with [249] entries. You should avoid
fetching large result sets as they can impact both the user experience and resource usage. For more info about this
event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#QueryResultListSizeEvent
2021-03-14 18:32:43.851 ERROR 28858 --- [nio-8092-exec-3] Hypersistence Optimizer                  : CRITICAL -
QueryResultListSizeEvent - The [select generatedAlias0 from Country as generatedAlias0] query returned a List with [249]
entries. You should avoid fetching large result sets as they can impact both the user experience and resource usage. For
more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#QueryResultListSizeEvent
ANTLR Tool version 4.8 used for code generation does not match the current runtime version 4.7.2ANTLR Tool version 4.8
used for code generation does not match the current runtime version 4.7.22021-03-14 18:32:55.941 ERROR 28858
--- [nio-8092-exec-4] Hypersistence Optimizer                  : CRITICAL - PaginationWithoutOrderByEvent -
The [SELECT registryentry_1 FROM RegistryEntry AS registryentry_1 JOIN registryentry_1.referenceSignOff AS signoff_1 WHERE registryentry_1.ownedBy = :parameter_0 and signoff_1.systemId = :parameter_1]
query uses pagination without an ORDER BY clause. Therefore, the result is not deterministic since SQL does not
guarantee any particular ordering unless an ORDER BY clause is being used. For more info about this event, check out
this User Guide link - https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#PaginationWithoutOrderByEvent
