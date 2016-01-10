#!/usr/bin/perl
use strict;
use Getopt::Std;
my %opt=();
getopts("obct", \%opt);
my $makefile="/home/alex/code/makefile";
my @files = </home/alex/code/*\.*>; 
my @cppFiles;
my $file;
sub makeFile($$);
my @oFiles;
my @debugFiles;
my @releaseFiles;
if($opt{t}){
my $cmd="cd /home/alex/code ; etags /home/alex/code/*/*.{h,cpp,sh,pl,py}"; #{c++/*.{h,cpp},shellScripts/*.sh,perl/*.pl,python/*.py}
system($cmd);
exit;
}
foreach $file (@files) {
    if($file=~m/(.cpp$)/){
	$file =~ s/\/home\/alex\/code\///;
	$file =~ m/(.*)\.cpp/;
	push(@cppFiles, $file);
	push(@oFiles, join('.', $1,"o"));
	push(@releaseFiles, join('-', $1,"x86_64-03"));
	push(@debugFiles, join('-', $1,"x86_64"));
    }
}
open FILE, ">$makefile" or die $!; print FILE "av"; close FILE;
my $cmd="cd /home/alex/code;make;"; #rm makefile; rm *.o
for (my $index=0;$index<@debugFiles;$index++)
{
    if($opt{b}){
	makeFile("d",$index);
	system($cmd);
	makeFile("o",$index);
	system($cmd);
    }elsif($opt{o}){
	makeFile("o",$index);
	system($cmd);
    }
    elsif($opt{c}){
	if($index eq 0){
	    system("cd /home/alex/code;rm makefile *-x86_64*");
	}
    }
    else{
	makeFile("d",$index);
	system($cmd);
    }
}
sub makeFile($$)
{
    my @whichFile;
    my $parms;
    my $index=$_[1];
    if($_[0] eq "d"){
	@whichFile=@debugFiles;
	$parms="-I /usr/local/boost_1_47_0 -g";
    }
    elsif($_[0] eq "o"){
	@whichFile=@releaseFiles;
	$parms="-I /usr/local/boost_1_47_0";
    }
    else{
	die $!;
    }
open FILE, ">$makefile" or die $!;
print FILE "CXX=g++ $parms -Wall -std=gnu++0x\n"; 
my $line=join(' ',$whichFile[$index],":",$oFiles[$index]);
print FILE "$line\n";
$line=join(' ',"\t","\$(CXX)","-o",$whichFile[$index],$oFiles[$index],"-I.");
print FILE "$line\n";
$line=join(' ',$oFiles[$index],":",$cppFiles[$index]);
print FILE "$line\n";
$line=join(' ',"\t","\$(CXX)","-c",$cppFiles[$index],"-I.");
print FILE "$line\n";
close FILE;
}

