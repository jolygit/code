use FileHandle;
use Carp;
use Encode;

my $fun=sub vova{
print "HELL\n" ;
};
&$fun;
# \G example #################################

unlink glob("~/*test.txt");
my @ls=("adf","","avdf");
print $ENV{USER};
while (@ls && $ls[0]=~ s/^a..//){
    print $_ ;
    redo if /^$/;
   
}
my %hs;
 $hs{'vova','gi'}=123;
my %list=('aabb','aaag');
my @l1=%list;
my $y=(join  => 'aabb','aaag');
my $x=($y);
my $match;
foreach  (@list){
$match = $1 if(?(aa.*)?)
}
print "$match\n";
my $str='aefelfghtrhhrbel';
print "$1\n" if $str=~/(?<=h.)(.el)/g;
print "$1\n" if $str=~/(\d+) /g;
print "$1\n" if $str=~/\G(\w+)/;
print "$1\n" if $str=~/(\w+) /g;
# Note that \G in this case could be droped if instead g was added to the /.
