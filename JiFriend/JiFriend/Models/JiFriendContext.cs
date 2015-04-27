using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Web;

namespace JiFriend.Models
{
    public class JiFriendContext : DbContext
    {
        public DbSet<User> Users { get; set; }
        public DbSet<SquareStatus> SquareStatuss { get; set; }
        public DbSet<Party> Partys { get; set; }
        public DbSet<SquarePrize> SquarePrizes { get; set; }
        public DbSet<PartyAttender> PartyAttenders { get; set; }
        public DbSet<Job> Jobs { get; set; }

        public JiFriendContext()
            : base("AndroidConnection")
        {

        }
    }
}