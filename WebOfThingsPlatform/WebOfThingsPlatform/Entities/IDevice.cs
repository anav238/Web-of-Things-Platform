using BrightstarDB.EntityFramework;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace WebOfThingsPlatform
{
    [Entity]
    interface IDevice
    {
        public Guid Id { get; set; }
        public string Title { get; set; }
        public string Description { get; set; }
        public CategoryEnum Category { get; set; }
        public string BaseLink { get; set; }
        public List<string> Properties { get; set; }
        public List<string> Actions { get; set; }
        public List<string> Events { get; set; }
        public List<string> Links { get; set; }
    }

    public enum CategoryEnum
    {
        Entertainment,
        Environment,
        Security
    }
}
