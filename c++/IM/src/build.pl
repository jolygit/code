#!/usr/bin/perl
use strict;
use Getopt::Std;

my $cmd="cd /home/alex/code/c++/IM/misc;make;";
system($cmd);
 $cmd="cd /home/alex/code/c++/IM/src;make;";
system($cmd);
