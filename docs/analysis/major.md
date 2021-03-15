# Problems marked major

2021-03-14 18:24:42.832 WARN 28858 --- [           main] Hypersistence Optimizer                  : MAJOR -
LargeColumnEvent - The [referenceArchiveUnitSystemId] attribute in the [aud_nikita.common.model.noark5.v5.ChangeLog_AUD]
entity is mapped to a large column type. Consider using either compact types or moving the large columns to separate
tables or using multiple entities mapped to the same database table so that you can choose which properties are to be
fetched from the database based on the entity type. You should use the @Basic(fetch=LAZY) annotation and activate the
bytecode enhancement lazy loading mechanism as, otherwise, the column is fetched eagerly when loading the entity. For
more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#LargeColumnEvent
2021-03-14 18:24:43.132 WARN 28858 --- [           main] Hypersistence Optimizer                  : MAJOR -
LargeColumnEvent - The [referenceArchiveUnitSystemId] attribute in the [nikita.common.model.noark5.v5.EventLog] entity
is mapped to a large column type. Consider using either compact types or moving the large columns to separate tables or
using multiple entities mapped to the same database table so that you can choose which properties are to be fetched from
the database based on the entity type. You should use the @Basic(fetch=LAZY) annotation and activate the bytecode
enhancement lazy loading mechanism as, otherwise, the column is fetched eagerly when loading the entity. You should use
the @DynamicUpdate annotation so that the UPDATE statement contains only the columns that have been modified by the
currently running Persistence Context. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#LargeColumnEvent
2021-03-14 18:24:43.416 WARN 28858 --- [           main] Hypersistence Optimizer                  : MAJOR -
EnumTypeStringEvent - The [authorityName] enum attribute in the [nikita.common.model.noark5.v5.admin.Authority] entity
uses the EnumType.STRING strategy, which has a bigger memory footprint than EnumType.ORDINAL. For more info about this
event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#EnumTypeStringEvent
2021-03-14 18:24:43.435 WARN 28858 --- [           main] Hypersistence Optimizer                  : MAJOR -
LargeColumnEvent - The [referencePrecedenceApprovedBySystemID] attribute in
the [aud_nikita.common.model.noark5.v5.secondary.Precedence_AUD] entity is mapped to a large column type. Consider using
either compact types or moving the large columns to separate tables or using multiple entities mapped to the same
database table so that you can choose which properties are to be fetched from the database based on the entity type. You
should use the @Basic(fetch=LAZY) annotation and activate the bytecode enhancement lazy loading mechanism as, otherwise,
the column is fetched eagerly when loading the entity. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#LargeColumnEvent
2021-03-14 18:24:43.518 WARN 28858 --- [           main] Hypersistence Optimizer                  : MAJOR -
LargeColumnEvent - The [referenceArchiveUnitSystemId] attribute in the [nikita.common.model.noark5.v5.ChangeLog] entity
is mapped to a large column type. Consider using either compact types or moving the large columns to separate tables or
using multiple entities mapped to the same database table so that you can choose which properties are to be fetched from
the database based on the entity type. You should use the @Basic(fetch=LAZY) annotation and activate the bytecode
enhancement lazy loading mechanism as, otherwise, the column is fetched eagerly when loading the entity. You should use
the @DynamicUpdate annotation so that the UPDATE statement contains only the columns that have been modified by the
currently running Persistence Context. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#LargeColumnEvent
2021-03-14 18:24:43.950 WARN 28858 --- [           main] Hypersistence Optimizer                  : MAJOR -
LargeColumnEvent - The [referencePrecedenceApprovedBySystemID] attribute in
the [nikita.common.model.noark5.v5.secondary.Precedence] entity is mapped to a large column type. Consider using either
compact types or moving the large columns to separate tables or using multiple entities mapped to the same database
table so that you can choose which properties are to be fetched from the database based on the entity type. You should
use the @Basic(fetch=LAZY) annotation and activate the bytecode enhancement lazy loading mechanism as, otherwise, the
column is fetched eagerly when loading the entity. You should use the @DynamicUpdate annotation so that the UPDATE
statement contains only the columns that have been modified by the currently running Persistence Context. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#LargeColumnEvent
2021-03-14 18:24:44.042 WARN 28858 --- [           main] Hypersistence Optimizer                  : MAJOR -
LargeColumnEvent - The [referenceFlowFromSystemID] attribute in
the [aud_nikita.common.model.noark5.v5.secondary.DocumentFlow_AUD] entity is mapped to a large column type. Consider
using either compact types or moving the large columns to separate tables or using multiple entities mapped to the same
database table so that you can choose which properties are to be fetched from the database based on the entity type. You
should use the @Basic(fetch=LAZY) annotation and activate the bytecode enhancement lazy loading mechanism as, otherwise,
the column is fetched eagerly when loading the entity. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#LargeColumnEvent
2021-03-14 18:24:44.043 WARN 28858 --- [           main] Hypersistence Optimizer                  : MAJOR -
LargeColumnEvent - The [referenceFlowToSystemID] attribute in
the [aud_nikita.common.model.noark5.v5.secondary.DocumentFlow_AUD] entity is mapped to a large column type. Consider
using either compact types or moving the large columns to separate tables or using multiple entities mapped to the same
database table so that you can choose which properties are to be fetched from the database based on the entity type. You
should use the @Basic(fetch=LAZY) annotation and activate the bytecode enhancement lazy loading mechanism as, otherwise,
the column is fetched eagerly when loading the entity. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#LargeColumnEvent
2021-03-14 18:24:44.073 WARN 28858 --- [           main] Hypersistence Optimizer                  : MAJOR -
LargeColumnEvent - The [referenceSignedOffCorrespondencePartSystemID] attribute in
the [aud_nikita.common.model.noark5.v5.secondary.SignOff_AUD] entity is mapped to a large column type. Consider using
either compact types or moving the large columns to separate tables or using multiple entities mapped to the same
database table so that you can choose which properties are to be fetched from the database based on the entity type. You
should use the @Basic(fetch=LAZY) annotation and activate the bytecode enhancement lazy loading mechanism as, otherwise,
the column is fetched eagerly when loading the entity. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#LargeColumnEvent
2021-03-14 18:24:44.074 WARN 28858 --- [           main] Hypersistence Optimizer                  : MAJOR -
LargeColumnEvent - The [referenceSignedOffRecordSystemID] attribute in
the [aud_nikita.common.model.noark5.v5.secondary.SignOff_AUD] entity is mapped to a large column type. Consider using
either compact types or moving the large columns to separate tables or using multiple entities mapped to the same
database table so that you can choose which properties are to be fetched from the database based on the entity type. You
should use the @Basic(fetch=LAZY) annotation and activate the bytecode enhancement lazy loading mechanism as, otherwise,
the column is fetched eagerly when loading the entity. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#LargeColumnEvent
2021-03-14 18:24:44.087 WARN 28858 --- [           main] Hypersistence Optimizer                  : MAJOR -
LargeColumnEvent - The [referenceArchiveUnitSystemId] attribute in the [aud_nikita.common.model.noark5.v5.EventLog_AUD]
entity is mapped to a large column type. Consider using either compact types or moving the large columns to separate
tables or using multiple entities mapped to the same database table so that you can choose which properties are to be
fetched from the database based on the entity type. You should use the @Basic(fetch=LAZY) annotation and activate the
bytecode enhancement lazy loading mechanism as, otherwise, the column is fetched eagerly when loading the entity. For
more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#LargeColumnEvent
2021-03-14 18:24:44.523 WARN 28858 --- [           main] Hypersistence Optimizer                  : MAJOR -
LargeColumnEvent - The [referenceFlowFromSystemID] attribute in
the [nikita.common.model.noark5.v5.secondary.DocumentFlow] entity is mapped to a large column type. Consider using
either compact types or moving the large columns to separate tables or using multiple entities mapped to the same
database table so that you can choose which properties are to be fetched from the database based on the entity type. You
should use the @Basic(fetch=LAZY) annotation and activate the bytecode enhancement lazy loading mechanism as, otherwise,
the column is fetched eagerly when loading the entity. You should use the @DynamicUpdate annotation so that the UPDATE
statement contains only the columns that have been modified by the currently running Persistence Context. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#LargeColumnEvent
2021-03-14 18:24:44.525 WARN 28858 --- [           main] Hypersistence Optimizer                  : MAJOR -
LargeColumnEvent - The [referenceFlowToSystemID] attribute in the [nikita.common.model.noark5.v5.secondary.DocumentFlow]
entity is mapped to a large column type. Consider using either compact types or moving the large columns to separate
tables or using multiple entities mapped to the same database table so that you can choose which properties are to be
fetched from the database based on the entity type. You should use the @Basic(fetch=LAZY) annotation and activate the
bytecode enhancement lazy loading mechanism as, otherwise, the column is fetched eagerly when loading the entity. You
should use the @DynamicUpdate annotation so that the UPDATE statement contains only the columns that have been modified
by the currently running Persistence Context. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#LargeColumnEvent
2021-03-14 18:24:44.576 WARN 28858 --- [           main] Hypersistence Optimizer                  : MAJOR -
LargeColumnEvent - The [referenceSignedOffCorrespondencePartSystemID] attribute in
the [nikita.common.model.noark5.v5.secondary.SignOff] entity is mapped to a large column type. Consider using either
compact types or moving the large columns to separate tables or using multiple entities mapped to the same database
table so that you can choose which properties are to be fetched from the database based on the entity type. You should
use the @Basic(fetch=LAZY) annotation and activate the bytecode enhancement lazy loading mechanism as, otherwise, the
column is fetched eagerly when loading the entity. You should use the @DynamicUpdate annotation so that the UPDATE
statement contains only the columns that have been modified by the currently running Persistence Context. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#LargeColumnEvent
2021-03-14 18:24:44.578 WARN 28858 --- [           main] Hypersistence Optimizer                  : MAJOR -
LargeColumnEvent - The [referenceSignedOffRecordSystemID] attribute in
the [nikita.common.model.noark5.v5.secondary.SignOff] entity is mapped to a large column type. Consider using either
compact types or moving the large columns to separate tables or using multiple entities mapped to the same database
table so that you can choose which properties are to be fetched from the database based on the entity type. You should
use the @Basic(fetch=LAZY) annotation and activate the bytecode enhancement lazy loading mechanism as, otherwise, the
column is fetched eagerly when loading the entity. You should use the @DynamicUpdate annotation so that the UPDATE
statement contains only the columns that have been modified by the currently running Persistence Context. For more info
about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#LargeColumnEvent
2021-03-14 18:24:45.112 WARN 28858 --- [           main] Hypersistence Optimizer                  : MAJOR -
DialectVersionEvent - Your application is using the [org.hibernate.dialect.MySQL5InnoDBDialect] Hibernate-specific
Dialect. Consider using the [org.hibernate.dialect.MySQL57Dialect] instead, as it's closer to your current database
server version [MySQL 5.7]. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#DialectVersionEvent
2021-03-14 18:24:45.113 WARN 28858 --- [           main] Hypersistence Optimizer                  : MAJOR -
SkipAutoCommitCheckEvent - You should set the [hibernate.connection.provider_disables_autocommit] configuration property
to [true] while also making sure that the underlying DataSource is configured to disable the auto-commit flag whenever a
new Connection is being acquired. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#SkipAutoCommitCheckEvent
2021-03-14 18:24:45.126 WARN 28858 --- [           main] Hypersistence Optimizer                  : MAJOR -
QueryInClauseParameterPaddingEvent - You should set the [hibernate.query.in_clause_parameter_padding] configuration
property to the value of [true], as Hibernate entity queries can then make better use of statement caching and fewer
entity queries will have to be compiled while varying the number of parameters passed to the in query clause. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#QueryInClauseParameterPaddingEvent
2021-03-14 18:24:45.129 WARN 28858 --- [           main] Hypersistence Optimizer                  : MAJOR -
DefaultAuditStrategyEvent - Your application is using the [org.hibernate.envers.strategy.DefaultAuditStrategy] Hibernate
Envers strategy. Consider setting the [org.hibernate.envers.audit_strategy] configuration property to the value
of [org.hibernate.envers.strategy.internal.ValidityAuditStrategy] instead, as it can generate more efficient audit log
queries. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#DefaultAuditStrategyEvent
2021-03-14 18:24:45.129 WARN 28858 --- [           main] Hypersistence Optimizer                  : 491 issues were
found: 1 BLOCKER, 377 CRITICAL, 19 MAJOR, 94 MINOR 2021-03-14 18:32:29.793 WARN 28858 --- [nio-8092-exec-3]
Hypersistence Optimizer                  : MAJOR - StatementlessConnectionEvent - The JDBC Connection was borrowed
for [18] ms, but no SQL statement was executed. For more info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:29.803 WARN 28858 --- [nio-8092-exec-4] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [2] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:29.819 WARN 28858 --- [nio-8092-exec-6] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [6] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:29.831 WARN 28858 --- [nio-8092-exec-7] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [3] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:29.840 WARN 28858 --- [nio-8092-exec-8] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [2] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:29.852 WARN 28858 --- [nio-8092-exec-9] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [3] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:29.863 WARN 28858 --- [io-8092-exec-10] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [3] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:29.873 WARN 28858 --- [nio-8092-exec-1] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [3] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:29.884 WARN 28858 --- [nio-8092-exec-2] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [3] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:29.892 WARN 28858 --- [nio-8092-exec-3] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [2] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:29.902 WARN 28858 --- [nio-8092-exec-4] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [2] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:30.397 WARN 28858 --- [nio-8092-exec-6] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [2] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:30.412 WARN 28858 --- [nio-8092-exec-8] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [5] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:30.427 WARN 28858 --- [io-8092-exec-10] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [5] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:30.445 WARN 28858 --- [nio-8092-exec-2] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [4] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:30.459 WARN 28858 --- [nio-8092-exec-4] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [4] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:30.470 WARN 28858 --- [nio-8092-exec-6] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [2] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:30.480 WARN 28858 --- [nio-8092-exec-8] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [2] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:31.435 WARN 28858 --- [nio-8092-exec-3] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [3] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:37.007 WARN 28858 --- [nio-8092-exec-7] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [2] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:37.080 WARN 28858 --- [nio-8092-exec-1] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:37.166 WARN 28858 --- [nio-8092-exec-5] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [2] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:37.228 WARN 28858 --- [nio-8092-exec-9] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:37.343 WARN 28858 --- [nio-8092-exec-3] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:38.278 WARN 28858 --- [nio-8092-exec-9] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [8] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:38.374 WARN 28858 --- [nio-8092-exec-3] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [10] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:38.455 WARN 28858 --- [nio-8092-exec-7] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [2] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:38.525 WARN 28858 --- [nio-8092-exec-1] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [3] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:38.598 WARN 28858 --- [nio-8092-exec-5] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [2] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:38.672 WARN 28858 --- [nio-8092-exec-9] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [3] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:38.748 WARN 28858 --- [nio-8092-exec-3] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [2] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:38.907 WARN 28858 --- [nio-8092-exec-1] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [2] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:39.009 WARN 28858 --- [nio-8092-exec-5] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:39.072 WARN 28858 --- [nio-8092-exec-9] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:39.123 WARN 28858 --- [nio-8092-exec-3] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:39.196 WARN 28858 --- [nio-8092-exec-7] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:39.264 WARN 28858 --- [nio-8092-exec-1] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:39.318 WARN 28858 --- [nio-8092-exec-5] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:39.628 WARN 28858 --- [nio-8092-exec-7] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:40.353 WARN 28858 --- [nio-8092-exec-3] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [8] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:40.762 WARN 28858 --- [nio-8092-exec-9] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [3] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:42.159 WARN 28858 --- [nio-8092-exec-5] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [11] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:43.171 WARN 28858 --- [io-8092-exec-10] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:43.180 WARN 28858 --- [nio-8092-exec-2] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:43.190 WARN 28858 --- [nio-8092-exec-4] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:43.198 WARN 28858 --- [nio-8092-exec-6] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [2] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:43.212 WARN 28858 --- [nio-8092-exec-8] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:43.220 WARN 28858 --- [io-8092-exec-10] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:43.230 WARN 28858 --- [nio-8092-exec-2] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [2] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:43.949 WARN 28858 --- [nio-8092-exec-7] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [2] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:44.255 WARN 28858 --- [nio-8092-exec-7] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [2] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:44.264 WARN 28858 --- [nio-8092-exec-9] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:44.283 WARN 28858 --- [nio-8092-exec-3] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:44.472 WARN 28858 --- [nio-8092-exec-7] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [2] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:44.484 WARN 28858 --- [nio-8092-exec-9] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:44.512 WARN 28858 --- [nio-8092-exec-3] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:44.711 WARN 28858 --- [nio-8092-exec-1] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:44.769 WARN 28858 --- [nio-8092-exec-7] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [2] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:44.802 WARN 28858 --- [nio-8092-exec-1] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:44.834 WARN 28858 --- [nio-8092-exec-5] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [2] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:44.958 WARN 28858 --- [nio-8092-exec-9] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:44.990 WARN 28858 --- [nio-8092-exec-3] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:45.035 WARN 28858 --- [nio-8092-exec-7] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:45.200 WARN 28858 --- [nio-8092-exec-2] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:45.236 WARN 28858 --- [nio-8092-exec-6] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:45.387 WARN 28858 --- [nio-8092-exec-3] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:45.400 WARN 28858 --- [nio-8092-exec-5] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [2] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:45.414 WARN 28858 --- [nio-8092-exec-7] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:45.425 WARN 28858 --- [nio-8092-exec-9] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:45.453 WARN 28858 --- [nio-8092-exec-3] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [2] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:45.465 WARN 28858 --- [nio-8092-exec-5] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:45.518 WARN 28858 --- [io-8092-exec-10] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:45.645 WARN 28858 --- [nio-8092-exec-1] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:45.686 WARN 28858 --- [nio-8092-exec-5] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [2] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:45.911 WARN 28858 --- [io-8092-exec-10] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:45.924 WARN 28858 --- [nio-8092-exec-2] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:45.935 WARN 28858 --- [nio-8092-exec-4] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:45.948 WARN 28858 --- [nio-8092-exec-6] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [2] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:45.974 WARN 28858 --- [io-8092-exec-10] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:45.986 WARN 28858 --- [nio-8092-exec-2] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:46.033 WARN 28858 --- [nio-8092-exec-7] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:46.152 WARN 28858 --- [nio-8092-exec-6] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [2] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:46.186 WARN 28858 --- [io-8092-exec-10] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:46.414 WARN 28858 --- [nio-8092-exec-6] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:46.424 WARN 28858 --- [nio-8092-exec-8] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:46.437 WARN 28858 --- [io-8092-exec-10] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:46.450 WARN 28858 --- [nio-8092-exec-2] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:46.475 WARN 28858 --- [nio-8092-exec-6] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:46.486 WARN 28858 --- [nio-8092-exec-8] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:46.523 WARN 28858 --- [nio-8092-exec-2] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [2] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:46.587 WARN 28858 --- [io-8092-exec-10] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:46.890 WARN 28858 --- [nio-8092-exec-6] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [2] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:46.909 WARN 28858 --- [nio-8092-exec-9] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:46.921 WARN 28858 --- [nio-8092-exec-1] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:46.936 WARN 28858 --- [nio-8092-exec-3] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [2] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:46.950 WARN 28858 --- [nio-8092-exec-5] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [2] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:46.974 WARN 28858 --- [nio-8092-exec-9] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:46.988 WARN 28858 --- [nio-8092-exec-1] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:47.239 WARN 28858 --- [nio-8092-exec-6] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:47.263 WARN 28858 --- [nio-8092-exec-9] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:47.275 WARN 28858 --- [nio-8092-exec-1] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:47.288 WARN 28858 --- [nio-8092-exec-3] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [2] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:47.301 WARN 28858 --- [nio-8092-exec-5] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:47.328 WARN 28858 --- [nio-8092-exec-9] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:47.343 WARN 28858 --- [nio-8092-exec-1] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [2] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:47.686 WARN 28858 --- [nio-8092-exec-8] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [2] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:47.709 WARN 28858 --- [nio-8092-exec-1] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:47.720 WARN 28858 --- [nio-8092-exec-3] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:47.731 WARN 28858 --- [nio-8092-exec-5] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:47.743 WARN 28858 --- [nio-8092-exec-7] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:47.772 WARN 28858 --- [nio-8092-exec-1] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:47.784 WARN 28858 --- [nio-8092-exec-3] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:47.841 WARN 28858 --- [nio-8092-exec-9] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [2] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:47.875 WARN 28858 --- [nio-8092-exec-3] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:48.098 WARN 28858 --- [nio-8092-exec-8] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:48.123 WARN 28858 --- [nio-8092-exec-1] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [2] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:48.133 WARN 28858 --- [nio-8092-exec-3] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:48.145 WARN 28858 --- [nio-8092-exec-5] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:48.157 WARN 28858 --- [nio-8092-exec-7] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:48.185 WARN 28858 --- [nio-8092-exec-1] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:48.198 WARN 28858 --- [nio-8092-exec-3] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [2] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:48.251 WARN 28858 --- [nio-8092-exec-9] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:48.288 WARN 28858 --- [nio-8092-exec-3] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:48.515 WARN 28858 --- [nio-8092-exec-7] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:48.544 WARN 28858 --- [nio-8092-exec-1] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:48.736 WARN 28858 --- [nio-8092-exec-1] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:48.763 WARN 28858 --- [nio-8092-exec-5] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:48.950 WARN 28858 --- [nio-8092-exec-5] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [2] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:48.978 WARN 28858 --- [nio-8092-exec-9] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:49.170 WARN 28858 --- [nio-8092-exec-9] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:49.201 WARN 28858 --- [nio-8092-exec-3] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:55.191 WARN 28858 --- [io-8092-exec-10] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:56.283 WARN 28858 --- [io-8092-exec-10] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:56.319 WARN 28858 --- [nio-8092-exec-4] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:56.350 WARN 28858 --- [nio-8092-exec-8] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:56.399 WARN 28858 --- [nio-8092-exec-2] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:56.442 WARN 28858 --- [nio-8092-exec-6] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:57.126 WARN 28858 --- [nio-8092-exec-2] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:57.195 WARN 28858 --- [nio-8092-exec-6] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:57.248 WARN 28858 --- [io-8092-exec-10] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [2] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:57.288 WARN 28858 --- [nio-8092-exec-4] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:57.332 WARN 28858 --- [nio-8092-exec-8] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:57.376 WARN 28858 --- [nio-8092-exec-2] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:57.423 WARN 28858 --- [nio-8092-exec-6] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:57.528 WARN 28858 --- [nio-8092-exec-4] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:57.606 WARN 28858 --- [nio-8092-exec-8] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:57.674 WARN 28858 --- [nio-8092-exec-2] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:57.750 WARN 28858 --- [nio-8092-exec-6] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:57.835 WARN 28858 --- [io-8092-exec-10] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:57.908 WARN 28858 --- [nio-8092-exec-4] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:57.958 WARN 28858 --- [nio-8092-exec-8] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:58.135 WARN 28858 --- [io-8092-exec-10] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:58.810 WARN 28858 --- [nio-8092-exec-6] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:32:59.091 WARN 28858 --- [nio-8092-exec-2] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:33:00.511 WARN 28858 --- [nio-8092-exec-8] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:33:08.081 WARN 28858 --- [nio-8092-exec-9] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:33:08.088 WARN 28858 --- [io-8092-exec-10] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [2] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:33:08.094 WARN 28858 --- [nio-8092-exec-1] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:33:08.101 WARN 28858 --- [nio-8092-exec-2] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:33:08.110 WARN 28858 --- [nio-8092-exec-3] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:33:08.117 WARN 28858 --- [nio-8092-exec-4] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:33:08.131 WARN 28858 --- [nio-8092-exec-6] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:33:08.179 WARN 28858 --- [io-8092-exec-10] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:33:08.216 WARN 28858 --- [nio-8092-exec-4] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:33:08.250 WARN 28858 --- [nio-8092-exec-8] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:33:08.288 WARN 28858 --- [nio-8092-exec-2] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:33:08.331 WARN 28858 --- [nio-8092-exec-6] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:33:08.365 WARN 28858 --- [io-8092-exec-10] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:33:08.400 WARN 28858 --- [nio-8092-exec-4] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:33:08.434 WARN 28858 --- [nio-8092-exec-8] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:33:08.472 WARN 28858 --- [nio-8092-exec-2] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:33:08.511 WARN 28858 --- [nio-8092-exec-6] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:33:08.546 WARN 28858 --- [io-8092-exec-10] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:33:08.580 WARN 28858 --- [nio-8092-exec-4] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:33:08.612 WARN 28858 --- [nio-8092-exec-8] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:33:08.649 WARN 28858 --- [nio-8092-exec-2] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:33:08.683 WARN 28858 --- [nio-8092-exec-6] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:33:08.723 WARN 28858 --- [io-8092-exec-10] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [2] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:33:08.762 WARN 28858 --- [nio-8092-exec-4] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:33:08.800 WARN 28858 --- [nio-8092-exec-8] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:33:08.836 WARN 28858 --- [nio-8092-exec-2] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:33:08.870 WARN 28858 --- [nio-8092-exec-6] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:33:08.908 WARN 28858 --- [io-8092-exec-10] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:33:08.945 WARN 28858 --- [nio-8092-exec-4] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:33:08.987 WARN 28858 --- [nio-8092-exec-8] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:33:09.024 WARN 28858 --- [nio-8092-exec-2] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:33:09.058 WARN 28858 --- [nio-8092-exec-6] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:33:09.093 WARN 28858 --- [io-8092-exec-10] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:33:09.119 WARN 28858 --- [nio-8092-exec-4] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:33:09.153 WARN 28858 --- [nio-8092-exec-8] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:33:09.189 WARN 28858 --- [nio-8092-exec-2] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:33:09.222 WARN 28858 --- [nio-8092-exec-6] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
2021-03-14 18:33:09.260 WARN 28858 --- [io-8092-exec-10] Hypersistence Optimizer                  : MAJOR -
StatementlessConnectionEvent - The JDBC Connection was borrowed for [1] ms, but no SQL statement was executed. For more
info about this event, check out this User Guide link
- https://vladmihalcea.com/hypersistence-optimizer/docs/user-guide/#StatementlessConnectionEvent
