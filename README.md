# nikita-noark5-core

<a href="https://scan.coverity.com/projects/OsloMet-ABI-nikita-noark5-core">
  <img alt="Coverity Scan Build Status"
       src="https://scan.coverity.com/projects/12784/badge.svg"/>
</a>

The need to preserve records is an endeavour mankind has undertaken for centuries. In a distributed cloud based world
the importance of record-keeping and the need to preserve records has become even more prevalent. Many are too reliant
on third parties (banks and email providers) to store their records. Nikita is a FOSS project that gives you the ability
to take control of your records and store them on your conditions, rather than being subject to the privacy unfriendly
conditions of others. Nikita also is based on a
standard ([Noark 5 REST API](https://github.com/arkivverket/noark5-tjenestegrensesnitt-standard)). Nikita implements a
REST based web API for storing and retrieving documents and metadata.

A twenty-minute video about nikita and open source record-keeping
from [debconf2020](https://debconf20.debconf.org/talks/58-flexible-record-keeping-in-a-foss-world/) gives an
introduction to what this software is about.

Read [INSTALL](docs/general/Install.md) to get started.

## Contributing

You are encouraged to contribute. You can either hack on the code and send pull requests or join us on
[OFTC IRC channel #nikita](https://webchat.oftc.net/?randomnick=1&channels=%23nikita&uio=d4)
for discussions. Feedback is greatly appreciated. The project has an open ethos and welcomes all forms of feedback. The
project maintains a mailing list
(https://lists.nuug.no/mailman/listinfo/nikita-noark) and issues can be raised 
via gitlab (https://gitlab.com/OsloMet-ABI/nikita-noark5-core/issues). Look at
[Starter bugs](./docs/general/Starter-bugs.md) for some suggestions on things
to do.

## Miscellaneous

For configuration purposes, take a look at the resources directory for setting properties in the various profiles.

The project is licensed under the AGPLv3 license.

Integration tests are being developed, however they do not cover all functionality yet. We currentlyuse the  
Noark5 API [tester](https://github.com/petterreinholdtsen/noark5-tester) developed by Petter Reinholdtsen. This should
give you a good idea about how well nikita works and how much of the Noark standard it implements.

Thanks to IntelliJ for an [idea](https://www.jetbrains.com/idea/) license.

