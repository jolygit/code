#!/usr/bin/perl
use strict;
use Getopt::Std;

my $cmd="cd ~/code/c++/IM/misc;make;";
system($cmd);
 $cmd="cd ~/code/c++/IM/src;make;";
system($cmd);
