# nikita-noark5-core

[![Build Status](https://travis-ci.org/OsloMet-ABI/nikita-noark5-core.svg?branch=master)](https://travis-ci.org/OsloMet-ABI/nikita-noark5-core)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/7a6f03be877e45f48448af68554b9413)](https://www.codacy.com/app/tsodring/nikita-noark5-core?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=OsloMet-ABI/nikita-noark5-core&amp;utm_campaign=Badge_Grade)
<a href="https://scan.coverity.com/projects/OsloMet-ABI-nikita-noark5-core">
  <img alt="Coverity Scan Build Status"
       src="https://scan.coverity.com/projects/12784/badge.svg"/>
</a>

This is an open source Noark 5v4 core.  This implement a REST based web API for storing av retrieving documents and metadata based on the Norwegian archive standard NOARK 5.

The core is very much under development and should be seen as a alpha product until releases closer to 0.6. The current version is 0.1 and has implemented the arkivstruktur interface of Noark 5v4.  

Read [INSTALL](docs/general/Install.md) to get started.

## Contributing

You are encouraged to contribute. You can either hack on the code and send pull requests or join us on [IRC](http://webchat.freenode.net?randomnick=1&channels=%23nikita&uio=d4) for discussions. Feedback is greatly appreciated. The project has an open ethos and welcomes all forms of feedback. The project maintains a mailing list (https://lists.nuug.no/mailman/listinfo/nikita-noark) and issues can be raised via gitlab (https://gitlab.com/OsloMet-ABI/nikita-noark5-core/issues). Look at [Starter bugs](./docs/general/Starter-bugs.md) for some suggestions on things to do.

## Miscellaneous

For configuration purposes, take a look at the resources directory of core-webapp for application-*.yml files for
setting properties in the various profiles.

The project has chosen the AGPLv3 license.

We are currently skipping tests as we had some issues before getting the test 
framework to run correctly. The test framework has been combined with 
spring-rest-docs and hopefully we can start developing a better testing 
framework. Currently there is no point running tests.

Thanks to IntelliJ for an [idea](https://www.jetbrains.com/idea/) license.
Thanks to GitLab for including the project in the [GitLab OSS program](https://gitlab.com/gitlab-com/gitlab-oss) and donating [Gold membership](https://about.gitlab.com/pricing/#gitlab-com) to the project.
