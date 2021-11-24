using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace WebOfThingsPlatform.Entities
{
    public class Home
    {
        public Guid Id { get; set; }
        public string Name { get; set; }
        public List<Guid> UserId { get; set; }
        public List<Guid> RoleId { get; set; }
        public List<Guid> DeviceId { get; set; }


    }
}
