# Hibernate analysis

Thomas bought a one year license for [hypersistence optimizer](https://vladmihalcea.com/hypersistence-optimizer) to test
the nikita domain model. I have for a long time believed there are problems and that we are experiencing
the [N+1 problem](https://vladmihalcea.com/n-plus-1-query-problem/) somewhere.

I ran runtest with hypersistence optimizer and filtered out the categories of problems so that we can document them here
in the codebase and deal with them one-by-one.

A summary shows the following has to be dealt with:

    491 issues were found: 1 BLOCKER, 377 CRITICAL, 19 MAJOR, 94 MINOR

The BLOCKER issue is that we do not use a tool like liquibase for database migration. The others are described in their
own files. 
